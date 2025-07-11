package com.example.ballkkaye.subscriber;

import com.example.ballkkaye.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Subscriber implements MessageListener {

    private final FcmService fcmService;


    // Redis 메시지 수신 시 자동 호출되는 콜백 메서드
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // [1] 채널 이름과 메시지 내용 추출
        String channel = new String(message.getChannel());  // 예: "today-game-updated"
        String payload = new String(message.getBody()); // 실제 publish된 메시지 내용 (예: "오늘의 경기가 업데이트 되었습니다!")

        log.info("Redis 메시지 수신 - 채널: {}, 메시지: {}", channel, payload);

        // [2] 채널 종류에 따라 푸시 메시지 전송
        switch (channel) {
            case "today-game-updated" -> fcmService.sendToUserRoleUsers("오늘의 경기가 업데이트 되었습니다!");
            case "hitter-lineup-updated" -> fcmService.sendToUserRoleUsers("오늘의 타자 라인업이 공개되었습니다!");
            case "starting-pitcher-lineup-updated" -> fcmService.sendToUserRoleUsers("오늘의 승리예측이 업데이트 되었습니다!");
            case "team-record-updated" -> fcmService.sendToUserRoleUsers("팀 기록이 업데이트되었습니다!");
            default -> log.warn("수신한 채널을 처리하지 못했습니다: {}", channel);
        }
    }
}
