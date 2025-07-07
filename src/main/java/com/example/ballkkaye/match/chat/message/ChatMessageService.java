package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public void save(ChatMessageRequest.DTO messageDTO, User sessionUser) {
        ChatRoom chatRoomPS = chatRoomRepository.findById(messageDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));
        ChatRoomUser chatRoomUserPS = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoomPS.getId()).orElseThrow(() -> new RuntimeException("채팅방에 존재하지 않음"));


        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoomPS)
                .user(userPS)
                .content(messageDTO.getMessage())
                .messageType(messageDTO.getMessageType())
                .build();

        chatMessageRepository.save(message);
    }
}

