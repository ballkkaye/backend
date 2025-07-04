package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;


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
                return new ChatRoomUserResponse.SaveDTO(existing);
            } else {
                throw new RuntimeException("이미 채팅방에 참여 중입니다.");
            }
        }

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .isOwner(false)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        chatRoomUserRepository.save(chatRoomUser);
        return new ChatRoomUserResponse.SaveDTO(chatRoomUser);
    }

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


        return new ChatRoomUserResponse.DeleteDTO();
    }
}
