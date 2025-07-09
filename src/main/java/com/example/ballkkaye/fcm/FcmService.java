package com.example.ballkkaye.fcm;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

    // 단일 대상 전송
    public void sendMessageTo(String targetToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ FCM 메시지 전송 완료: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM 전송 실패: " + e.getMessage());
        }
    }

    // 여러 대상 전송
    public void sendMulticastMessageTo(List<String> targetTokens, String title, String body, String url) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .putData("url", url)
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println("✅ FCM 일괄 전송 성공: " + response.getSuccessCount() + "명에게 알림 전송됨");
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM 일괄 전송 실패: " + e.getMessage());
        }
    }
}
