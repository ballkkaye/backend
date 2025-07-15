package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.ChatSessionManager;
import com.example.ballkkaye.common.enums.ChatConnectedType;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRequest;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatSessionManager chatSessionManager;

    @Transactional // TODO 유효성 검사 로직 추가해야 됨
    public ChatMessageResponse.DTO save(ChatMessageRequest.DTO reqDTO, User sessionUser) {
        log.info("[채팅메시지 저장 요청] 채팅방 ID: {}, 보낸 유저 ID: {}, 메시지 타입: {}, 내용: {}",
                reqDTO.getChatRoomId(), sessionUser.getId(), reqDTO.getMessageType(), reqDTO.getMessage());


        ChatRoom chatRoomPS = chatRoomRepository.findById(reqDTO.getChatRoomId())
                .orElseThrow(() -> {
                    log.warn("[채팅방 없음] ID: {}", reqDTO.getChatRoomId());
                    return new ExceptionApi404("채팅방 없음");
                });

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("[유저 없음] ID: {}", sessionUser.getId());
                    return new ExceptionApi404("유저 없음");
                });
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomPS.getId())
                .orElseThrow(() -> {
                    log.warn("[채팅방 참여자 아님] 유저 ID: {}, 채팅방 ID: {}", userPS.getId(), chatRoomPS.getId());
                    return new ExceptionApi404("채팅방에 존재하지 않음");
                });

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(reqDTO.getMessage())
                .messageType(reqDTO.getMessageType())
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        chatMessageRepository.save(message);

        log.info("[채팅메시지 저장 완료] 메시지 ID: {}, 채팅방 ID: {}, 보낸 유저: {}, 생성 시간: {}",
                message.getId(), chatRoomPS.getId(), userPS.getNickname(), message.getCreatedAt());


        return new ChatMessageResponse.DTO(
                message.getId(),
                reqDTO.getChatRoomId(),
                userPS.getId(),
                userPS.getNickname(),
                reqDTO.getMessage(),
                reqDTO.getMessageType(),
                true,
                message.getCreatedAt()
        );
    }

    // 중복 구독 방지용
    public Object handleAuth(ChatRoomUserRequest.AuthDTO reqDTO, User sessionUser) {
        boolean isNew = chatSessionManager.addSubscriber(reqDTO.getRoomId(), sessionUser.getId());

        log.info("[채팅 구독 요청] 채팅방 ID: {}, 유저 ID: {}, 중복 구독 여부: {}",
                reqDTO.getRoomId(), sessionUser.getId(), !isNew ? "중복" : "신규");


        Boolean isOwner = false;
        Optional<ChatRoomUser> optional = chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), reqDTO.getRoomId());
        if (optional.isPresent()) {
            isOwner = optional.get().getIsOwner();
            log.info("[방장 여부 확인] 유저 ID: {}, 채팅방 ID: {}, isOwner: {}", sessionUser.getId(), reqDTO.getRoomId(), isOwner);
        } else {
            log.warn("[채팅방 참여자 아님] 유저 ID: {}, 채팅방 ID: {}", sessionUser.getId(), reqDTO.getRoomId());
        }

        ChatConnectedType type = isNew ? ChatConnectedType.AUTH_SUCCESS : ChatConnectedType.AUTH_FAIL;

        log.info("[구독 결과] 유저 ID: {}, 채팅방 ID: {}, 결과: {}",
                sessionUser.getId(), reqDTO.getRoomId(), type);

        return new ChatMessageResponse.DTO(
                null,
                reqDTO.getRoomId(),
                sessionUser.getId(),
                sessionUser.getUsername(),
                null,
                type,
                isOwner,
                null
        );
    }


    // 메시지 List로 반환
    public List<ChatMessageResponse.DTO> getMessages(Integer roomId, User sessionUser) {
        chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), roomId)
                .orElseThrow(() -> {
                    log.warn("[메시지 조회 실패] 권한 없음 - 유저 ID: {}, 채팅방 ID: {}", sessionUser.getId(), roomId);
                    return new ExceptionApi403("권한 없음");
                });



        Timestamp connectedAt = chatRoomUserRepository.findCreatedAt(roomId, sessionUser.getId());
        log.debug("[접속 기준 시각] 유저 ID: {}, 채팅방 ID: {}, connectedAt: {}", sessionUser.getId(), roomId, connectedAt);

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdAndCreatedAtAfter(roomId, connectedAt);
        log.info("[메시지 조회 완료] 유저 ID: {}, 채팅방 ID: {}, 조회된 메시지 수: {}", sessionUser.getId(), roomId, messages.size());

        return messages.stream()
                .map(m -> new ChatMessageResponse.DTO(
                        m.getId(),
                        m.getChatRoom().getId(),
                        m.getUser().getId(),
                        m.getUser().getNickname(),
                        m.getContent(),
                        m.getMessageType(),
                        m.getUser().getId().equals(sessionUser.getId()),
                        m.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Object delete(Integer chatMessageId, User sessionUser) {
        // 채팅 조회
        ChatMessage chatMessagePS = chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> {
                    log.warn("[삭제 실패] 메시지 없음 - 메시지 ID: {}, 유저 ID: {}", chatMessageId, sessionUser.getId());
                    return new ExceptionApi404("해당 자원이 없습니다.");
                });

        // 권한 조회
        if (!chatMessagePS.getUser().getId().equals(sessionUser.getId())) {
            log.warn("[삭제 실패] 권한 없음 - 메시지 ID: {}, 요청자 ID: {}, 메시지 작성자 ID: {}",
                    chatMessageId, sessionUser.getId(), chatMessagePS.getUser().getId());
            throw new ExceptionApi403("권한이 없습니다.");
        }

        if (chatMessagePS.getDeleteStatus() == DeleteStatus.DELETED) {
            log.info("[삭제 요청 무시] 이미 삭제된 메시지 - 메시지 ID: {}, 유저 ID: {}", chatMessageId, sessionUser.getId());
            throw new ExceptionApi404("해당 자원이 없습니다.");
        }

        chatMessagePS.delete();
        log.info("[메시지 삭제 완료] 메시지 ID: {}, 유저 ID: {}", chatMessageId, sessionUser.getId());
        return new ChatMessageResponse.DeleteDTO(DeleteStatus.DELETED);
    }
}

