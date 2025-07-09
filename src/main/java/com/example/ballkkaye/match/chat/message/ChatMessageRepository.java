package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye.common.enums.DeleteStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {
    private final EntityManager em;

    public List<ChatMessage> findByChatRoomId(Integer chatRoomId) {
        String q = "SELECT cm FROM ChatMessage cm " +
                "WHERE cm.chatRoom.id = :chatRoomId AND cm.deleteStatus = :status";

        return em.createQuery(q, ChatMessage.class)
                .setParameter("chatRoomId", chatRoomId)
                .setParameter("status", DeleteStatus.NOT_DELETED)
                .getResultList();
    }
}
