package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
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
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 자원이 존재하지 않습니다."));

        Optional<ChatRoomUser> chatRoomUserOP = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomId);

        if (chatRoomUserOP.isPresent()) {
            ChatRoomUser existing = chatRoomUserOP.get();
            if (existing.getDeleteStatus().equals(DeleteStatus.DELETED)) {
                existing.setDeleteStatus(DeleteStatus.NOT_DELETED);

                ChatMessage message = ChatMessage.builder()
                        .chatRoom(chatRoomPS)
                        .user(userPS)
                        .content(userPS.getNickname() + "님이 입장하셨습니다.")
                        .messageType(ChatConnectedType.ENTER)
                        .deleteStatus(DeleteStatus.NOT_DELETED)
                        .build();
                chatMessageRepository.save(message);

                ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(
                        message.getId(),
                        chatRoomId,
                        userPS.getId(),
                        userPS.getNickname(),
                        message.getContent(),
                        ChatConnectedType.ENTER,
                        true,
                        message.getCreatedAt()
                );

                messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, response);
                return new ChatRoomUserResponse.SaveDTO(existing);
            } else {
                throw new ExceptionApi400("잘못된 요청입니다.");
            }
        }

        if (chatRoomPS.getIsSameTeam() && !userPS.getTeam().getId().equals(chatRoomPS.getTeam().getId())) {
            throw new ExceptionApi400("입장 제한: 채팅방 팀 조건에 맞지 않습니다.");
        }

        if (chatRoomUserRepository.countByChatRoomId(chatRoomId) >= chatRoomPS.getMaxParticipants()) {
            throw new ExceptionApi400("입장 제한: 채팅방 인원이 가득 찼습니다.");
        }

        if (!chatRoomPS.getPreferredGender().equals(Gender.NONE) &&
                !chatRoomPS.getPreferredGender().equals(userPS.getGender())) {
            throw new ExceptionApi400("입장 제한: 채팅방 성별 조건에 맞지 않습니다.");
        }

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
                throw new ExceptionApi400("입장 제한: 채팅방 연령 조건에 맞지 않습니다.");
            }
        }

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .isOwner(false)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        chatRoomUserRepository.save(chatRoomUser);

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(userPS.getNickname() + "님이 입장하셨습니다.")
                .messageType(ChatConnectedType.ENTER)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        chatMessageRepository.save(message);

        ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(
                message.getId(),
                chatRoomId,
                userPS.getId(),
                userPS.getNickname(),
                message.getContent(),
                ChatConnectedType.ENTER,
                true,
                message.getCreatedAt()
        );
        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, response);
        return new ChatRoomUserResponse.SaveDTO(chatRoomUser);
    }


    // 채팅방 퇴장
    @Transactional
    public Object delete(Integer chatRoomUserId, User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findById(chatRoomUserId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        ChatRoom chatRoomPS = chatRoomRepository.findById(chatRoomUserPS.getChatRoom().getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        chatRoomUserPS.delete();

        if (chatRoomUserRepository.countByChatRoomIdAndDeleteStatus(chatRoomUserPS.getChatRoom().getId(), DeleteStatus.NOT_DELETED) == 0) {
            chatRoomPS.delete();
        }

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(userPS.getNickname() + "님이 퇴장하셨습니다.")
                .messageType(ChatConnectedType.LEAVE)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        chatMessageRepository.save(message);

        ChatMessageResponse.DTO response = new ChatMessageResponse.DTO(
                message.getId(),
                chatRoomUserPS.getChatRoom().getId(),
                userPS.getId(),
                userPS.getNickname(),
                message.getContent(),
                ChatConnectedType.LEAVE,
                true,
                message.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/sub/chat/" + response.getChatRoomId(), response);
        return new ChatRoomUserResponse.DeleteDTO();
    }
}