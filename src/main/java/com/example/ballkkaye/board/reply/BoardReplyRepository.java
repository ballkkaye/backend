package com.example.ballkkaye.board.reply;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardReplyRepository {
    private final EntityManager em;

    public Long totalCount(Integer boardId) {
        String q = "SELECT COUNT(c) FROM BoardReply c WHERE c.board.id = :boardId";
        return em.createQuery(q, Long.class)
                .setParameter("boardId", boardId)
                .getSingleResult();
    }
}
