package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.Match;
import com.example.ballkkaye.match.MatchRepository;
import com.example.ballkkaye.match.chat.message.ChatMessage;
import com.example.ballkkaye.match.chat.message.ChatMessageRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserResponse;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Object delete(Integer chatRoomId, User sessionUser) {
        log.info("[채팅방 삭제 요청] 채팅방 ID: {}, 요청자 ID: {}", chatRoomId, sessionUser.getId());


        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.warn("[삭제 실패] 채팅방 없음 - 채팅방 ID: {}", chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(sessionUser.getId(), chatRoomId)
                .orElseThrow(() -> {
                    log.warn("[삭제 실패] 참여자 아님 - 유저 ID: {}, 채팅방 ID: {}", sessionUser.getId(), chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        if (!chatRoomUserPS.getIsOwner()) {
            log.warn("[삭제 실패] 권한 없음 - 유저 ID: {}, 채팅방 ID: {}", sessionUser.getId(), chatRoomId);
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }


        Match matchPS = matchRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> {
                    log.warn("[삭제 실패] 매치 없음 - 채팅방 ID: {}", chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 채팅방 유저 목록 조회
        List<ChatRoomUser> chatRoomUsersPS = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(matchPS.getChatRoom().getId());
        log.info("[삭제 대상 확인] 채팅방 ID: {}, 참여 유저 수: {}", chatRoomId, chatRoomUsersPS.size());


        matchPS.delete();
        chatRoomPS.delete();
        for (ChatRoomUser chatRoomUser : chatRoomUsersPS) {
            chatRoomUser.delete();
            log.debug("[참여자 삭제 처리] 유저 ID: {}", chatRoomUser.getUser().getId());
        }


        log.info("[채팅방 삭제 완료] 채팅방 ID: {}, 삭제자 ID: {}", chatRoomId, sessionUser.getId());
        return new ChatRoomResponse.DeleteDTO(DeleteStatus.DELETED);
    }

    @Transactional
    public void handleUserLeft(User sessionUser, Integer chatRoomId) {
        log.info("[채팅방 퇴장 요청] 유저 ID: {}, 채팅방 ID: {}", sessionUser.getId(), chatRoomId);

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("[퇴장 실패] 유저 없음 - ID: {}", sessionUser.getId());
                    return new ExceptionApi404("유저 없음");
                });
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.warn("[퇴장 실패] 채팅방 없음 - ID: {}", chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomId)
                .orElseThrow(() -> {
                    log.warn("[퇴장 실패] 채팅방 참여자 아님 - 유저 ID: {}, 채팅방 ID: {}", userPS.getId(), chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        chatRoomUserPS.updateLastDisconnectedAt();
        log.info("[채팅방 퇴장 완료] 유저 ID: {}, 채팅방 ID: {}", userPS.getId(), chatRoomId);
    }


    public Object chatrooms(User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("[채팅방 목록 조회 실패] 유저 없음 - ID: {}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        List<ChatRoomResponse.ItemDTO> respDTO = new ArrayList<>();
        List<ChatRoomUser> userChatRooms = chatRoomUserRepository.findByUserId(userPS.getId());

        log.info("[채팅방 목록 조회] 유저 ID: {}, 참여 중 채팅방 수: {}", userPS.getId(), userChatRooms.size());

        for (ChatRoomUser chatRoomUser : userChatRooms) {
            List<ChatRoomUserResponse.ProfileImgDTO> profileImages = new ArrayList<>();
            Boolean isOwner = chatRoomUser.getIsOwner();

            // 채팅 참여중인 유저들 조회
            List<ChatRoomUser> chatRoomParticipants = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(chatRoomUser.getChatRoom().getId());

            // 채팅 참여 중인 유저들 프로플 주워담기
            for (ChatRoomUser participant : chatRoomParticipants) {
                ChatRoomUserResponse.ProfileImgDTO profileImgDTO = new ChatRoomUserResponse.ProfileImgDTO(participant.getUser());
                profileImages.add(profileImgDTO);
            }


            String latestMessageTime = "";
            String latestMessageContent = "";

            ChatMessage chatMessagePS = chatMessageRepository.findLatestByChatRoomIdAndDeleteStatus(chatRoomUser.getChatRoom().getId());
            ChatRoom chatRoom = chatRoomUser.getChatRoom();
            Match matchPS = matchRepository.findByChatRoomId(chatRoom.getId())
                    .orElseThrow(() -> {
                        log.warn("[채팅방 매치 없음] 채팅방 ID: {}", chatRoom.getId());
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
            if (chatMessagePS != null) {
                latestMessageTime = p.format(new Date(chatMessagePS.getCreatedAt().getTime()));
                latestMessageContent = chatMessagePS.getContent();
            }

            log.debug("[채팅방 항목] 채팅방 ID: {}, 최근 메시지: {}", chatRoom.getId(), latestMessageContent);

            ChatRoomResponse.ItemDTO dto = new ChatRoomResponse.ItemDTO(chatRoom.getId(), matchPS.getTitle(), latestMessageTime, latestMessageContent, profileImages, isOwner);
            respDTO.add(dto);
        }
        log.info("[채팅방 목록 응답 완료] 유저 ID: {}, 응답 채팅방 수: {}", userPS.getId(), respDTO.size());
        return respDTO;
    }
}
