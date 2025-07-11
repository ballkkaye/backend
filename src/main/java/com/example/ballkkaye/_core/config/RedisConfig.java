package com.example.ballkkaye._core.config;


import com.example.ballkkaye.subscriber.Subscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    /**
     * Redis Pub/Sub 리스너 컨테이너 설정
     * - Redis의 여러 채널(topic)을 구독하여, 메시지가 수신되면 Subscriber 빈이 처리
     * - RedisMessageListenerContainer는 백그라운드에서 Redis 메시지를 비동기 수신
     * 채널별 용도:
     * - today-game-updated: 오늘 경기 정보 변경 알림
     * - hitter-lineup-updated: 타자 라인업 갱신 알림
     * - starting-pitcher-lineup-updated: 선발투수 라인업 갱신 알림
     * - team-record-updated: 팀 기록 갱신 알림
     */
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, Subscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 각 채널에 대해 subscriber를 리스너로 등록
        container.addMessageListener(subscriber, new ChannelTopic("today-game-updated"));
        container.addMessageListener(subscriber, new ChannelTopic("hitter-lineup-updated"));
        container.addMessageListener(subscriber, new ChannelTopic("starting-pitcher-lineup-updated"));
        container.addMessageListener(subscriber, new ChannelTopic("team-record-updated"));

        return container;
    }
}
