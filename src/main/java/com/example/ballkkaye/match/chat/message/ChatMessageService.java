package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.ChatSessionManager;
import com.example.ballkkaye.common.enums.ChatConnectedType;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRequest;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatSessionManager chatSessionManager;

    @Transactional
    public ChatMessageResponse.DTO save(ChatMessageRequest.DTO reqDTO, User sessionUser) {
        ChatRoom chatRoomPS = chatRoomRepository.findById(reqDTO.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomPS.getId())
                .orElseThrow(() -> new RuntimeException("채팅방에 존재하지 않음"));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(reqDTO.getMessage())
                .messageType(reqDTO.getMessageType())
                .build();

        chatMessageRepository.save(message);

        return new ChatMessageResponse.DTO(
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

        Boolean isOwner = false;
        Optional<ChatRoomUser> optional = chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), reqDTO.getRoomId());
        if (optional.isPresent()) {
            isOwner = optional.get().getIsOwner();
        }

        ChatConnectedType type = isNew ? ChatConnectedType.AUTH_SUCCESS : ChatConnectedType.AUTH_FAIL;

        return new ChatMessageResponse.DTO(
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
        Timestamp lastConnectedAt = chatRoomUserRepository.findLastConnectedAt(roomId, sessionUser.getId());

        List<ChatMessage> messages;
        if (lastConnectedAt == null) {
            messages = chatMessageRepository.findAllByRoomId(roomId);
        } else {
            messages = chatMessageRepository.findAllByRoomIdAndCreatedAtAfter(roomId, lastConnectedAt);
        }

        return messages.stream()
                .map(m -> new ChatMessageResponse.DTO(
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
}

