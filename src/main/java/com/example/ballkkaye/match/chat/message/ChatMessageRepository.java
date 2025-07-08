package com.example.ballkkaye.match.chat.message;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {
    private final EntityManager em;

    public void save(ChatMessage message) {
        em.persist(message);
    }

    public List<ChatMessage> findAllByRoomIdAndCreatedAtAfter(Integer roomId, Timestamp timestamp) {
        return em.createQuery("""
                            SELECT m FROM ChatMessage m
                            WHERE m.chatRoom.id = :roomId
                            AND m.createdAt > :timestamp
                            ORDER BY m.id ASC
                        """, ChatMessage.class)
                .setParameter("roomId", roomId)
                .setParameter("timestamp", timestamp)
                .getResultList();
    }

    public List<ChatMessage> findAllByRoomId(Integer roomId) {
        return em.createQuery("""
                            SELECT m FROM ChatMessage m
                            WHERE m.chatRoom.id = :roomId
                            ORDER BY m.id ASC
                        """, ChatMessage.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }

    public Optional<ChatMessage> findById(Integer chatMessageId) {
        ChatMessage chatMessage = em.find(ChatMessage.class, chatMessageId);
        return Optional.ofNullable(chatMessage);
    }
}
