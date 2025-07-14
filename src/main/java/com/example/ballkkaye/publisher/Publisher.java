package com.example.ballkkaye.publisher;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye.match.chat.message.ChatMessage;
import com.example.ballkkaye.match.chat.message.ChatMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Publisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // 한 번만 생성해서 재사용

    public void publishNewMessage(ChatMessage message) {
        String json = toJson(message);
        redisTemplate.convertAndSend("chat-message-created", json);
    }

    private String toJson(ChatMessage message) {
        ChatMessageResponse.ChatPublishDTO dto = new ChatMessageResponse.ChatPublishDTO(
                message.getChatRoom().getId(),
                message.getUser().getId(),
                message.getUser().getNickname(),
                message.getContent(),
                message.getMessageType()
        );

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new ExceptionApi400("메시지 직렬화 실패" + e.getMessage());
        }
    }
}
