package com.example.ballkkaye.match.chat.room;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
