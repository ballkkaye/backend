package com.example.ballkkaye.fcm;

import com.example.ballkkaye.match.chat.message.ChatMessageResponse;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.user.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

    private final ChatRoomRepository chatRoomRepository;

    public void sendMessage(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isBlank()) return;

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(fcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 단건 전송 완료: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 단건 전송 실패 ({}): {}", fcmToken, e.getMessage());
        }
    }

    public void sendToChatRoomUsers(ChatMessageResponse.ChatPublishDTO dto) {
        List<User> receivers = chatRoomRepository.findAllUsersByChatRoomId(dto.getChatRoomId()).stream()
                .filter(user -> !user.getId().equals(dto.getSenderId()))
                .toList();

        for (User user : receivers) {
            sendMessage(user.getFcmToken(), "Ballkkaye", "[" + dto.getSenderNickname() + "] " + dto.getMessage());
        }
    }

}
