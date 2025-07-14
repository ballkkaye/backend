package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private final EntityManager em;

    public void save(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

    public Optional<ChatRoom> findById(Integer chatRoomId) {
        return Optional.ofNullable(em.find(ChatRoom.class, chatRoomId));
    }


    // 해당 채팅방의 모든 참여자(User) 조회
    public List<User> findAllUsersByChatRoomId(Integer chatRoomId) {
        return em.createQuery("""
                            SELECT cru.user
                            FROM ChatRoomUser cru
                            WHERE cru.chatRoom.id = :chatRoomId
                        """, User.class)
                .setParameter("chatRoomId", chatRoomId)
                .getResultList();
    }
}
