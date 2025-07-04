package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.common.enums.DeleteStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ChatRoomUserRepository {

    private final EntityManager em;

    public void save(ChatRoomUser chatRoomUser) {
        em.persist(chatRoomUser);
    }

    public Optional<ChatRoomUser> findByUserIdAndChatRoomId(Integer userId, Integer chatRoomId) {
        try {
            ChatRoomUser result = em.createQuery("""
                            SELECT cru FROM ChatRoomUser cru
                            WHERE cru.user.id = :userId AND cru.chatRoom.id = :chatRoomId
                            """, ChatRoomUser.class)
                    .setParameter("userId", userId)
                    .setParameter("chatRoomId", chatRoomId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Integer countByChatRoomId(Integer chatRoomId) {
        return em.createQuery("""
                        SELECT COUNT(cru) FROM ChatRoomUser cru
                        WHERE cru.chatRoom.id = :chatRoomId AND cru.deleteStatus = 'NOT_DELETED'
                        """, Long.class)
                .setParameter("chatRoomId", chatRoomId)
                .getSingleResult().intValue();
    }

    public List<ChatRoomUser> findByChatRoomIdAndDeleteStatus(Integer chatRoomId) {
        String q = "SELECT cru FROM ChatRoomUser cru " +
                "WHERE cru.chatRoom.id = :chatRoomId AND cru.deleteStatus = :status";

        return em.createQuery(q, ChatRoomUser.class)
                .setParameter("chatRoomId", chatRoomId)
                .setParameter("status", DeleteStatus.NOT_DELETED)
                .getResultList();
    }
}
