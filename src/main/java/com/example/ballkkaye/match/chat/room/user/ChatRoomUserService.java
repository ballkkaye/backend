package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.ChatConnectedType;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.match.chat.message.ChatMessage;
import com.example.ballkkaye.match.chat.message.ChatMessageRepository;
import com.example.ballkkaye.match.chat.message.ChatMessageResponse;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;


    // 채팅방 입장
    @Transactional
    public Object save(Integer chatRoomId, User sessionUser) {
        // 유저 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));
        // 채팅방 조회
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));

        // 채팅방 유저 조회
        Optional<ChatRoomUser> chatRoomUserOP = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomId);

        // 채팅방 나간 유저 조회 나간 유저가 다시 들어온거면 DeleteStatus 상태 업데이트
        if (chatRoomUserOP.isPresent()) {
            ChatRoomUser existing = chatRoomUserOP.get();
            if (existing.getDeleteStatus().equals(DeleteStatus.DELETED)) {
                existing.setDeleteStatus(DeleteStatus.NOT_DELETED);

                ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(
                        chatRoomId,
                        userPS.getId(),
                        userPS.getNickname(),
                        userPS.getNickname() + "님이 입장하셨습니다.",
                        ChatConnectedType.ENTER
                );

                messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, response);

                return new ChatRoomUserResponse.SaveDTO(existing);
            } else {
                throw new RuntimeException("이미 채팅방에 참여 중입니다.");
            }
        }

        // isSameTeam 필터
        if (chatRoomPS.getIsSameTeam()) {
            if (!userPS.getTeam().getId().equals(chatRoomPS.getTeam().getId())) {
                throw new RuntimeException("같은 팀 유저만 참여할 수 있습니다.");
            }
        }

        // 인원수 필터
        if (chatRoomUserRepository.countByChatRoomId(chatRoomId) >= chatRoomPS.getMaxParticipants()) {
            throw new RuntimeException("채팅방 인원이 가득 찼습니다.");
        }

        // 성별 필터
        if (!chatRoomPS.getPreferredGender().equals(Gender.NONE)) {
            if (!chatRoomPS.getPreferredGender().equals(userPS.getGender())) {
                throw new RuntimeException("채팅방 성별 조건과 일치하지 않습니다.");
            }
        }

        // 나이 필터
        if (!chatRoomPS.getPreferredAge().equals(Age.NONE)) {
            int userAge = Period.between(userPS.getBirthDate(), LocalDate.now()).getYears();

            boolean isAllowed = switch (chatRoomPS.getPreferredAge()) {
                case UNDER_20 -> userAge < 20;
                case FROM_20_TO_30 -> userAge >= 20 && userAge < 30;
                case FROM_30_TO_40 -> userAge >= 30 && userAge < 40;
                case OVER_40 -> userAge >= 40;
                default -> true;
            };

            if (!isAllowed) {
                throw new RuntimeException("입장 제한: 채팅방 연령 조건에 맞지 않습니다.");
            }
        }


        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .isOwner(false)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        chatRoomUserRepository.save(chatRoomUser);

        // 입장 메시지 전송
        ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(chatRoomId, userPS.getId(), userPS.getNickname(), userPS.getNickname() + "님이 입장하셨습니다.", ChatConnectedType.ENTER);

        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, response); // 실시간 전송

        // DB 저장
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(response.getMessage())
                .messageType(ChatConnectedType.ENTER)
                .build();
        chatMessageRepository.save(message);
        return new ChatRoomUserResponse.SaveDTO(chatRoomUser);
    }


    // 채팅방 퇴장
    @Transactional
    public Object delete(Integer chatRoomUserId, User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findById(chatRoomUserId)
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));
        chatRoomUserPS.delete();
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomUserPS.getChatRoom().getId())
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));
        Integer countUser = chatRoomUserRepository.countByChatRoomIdAndDeleteStatus(chatRoomUserPS.getChatRoom().getId(), DeleteStatus.NOT_DELETED).intValue();
        if (countUser == 0) {
            chatRoomPS.delete();
        }

        // 퇴장 메시지 전송
        ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(
                chatRoomUserPS.getChatRoom().getId(),
                userPS.getId(),
                userPS.getNickname(),
                userPS.getNickname() + "님이 퇴장하셨습니다.",
                ChatConnectedType.LEAVE

        );

        messagingTemplate.convertAndSend("/sub/chat/" + response.getChatRoomId(), response); // 실시간 전송

        // DB 저장
        ChatMessage leaveMessage = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(response.getMessage())
                .messageType(ChatConnectedType.LEAVE)
                .build();
        chatMessageRepository.save(leaveMessage);


        return new ChatRoomUserResponse.DeleteDTO();
    }
}