package com.example.ballkkaye.board.reply.like;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardReplyLikeRepository {
    private final EntityManager em;


    public Optional<BoardReplyLike> findByReplyIdAndUserId(Integer replyId, Integer userId) {
        try {
            BoardReplyLike replyLike = em.createQuery(
                            "SELECT brl FROM BoardReplyLike brl WHERE brl.boardReply.id = :replyId AND brl.user.id = :userId",
                            BoardReplyLike.class)
                    .setParameter("replyId", replyId)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(replyLike);
        } catch (NoResultException e) {
            return Optional.ofNullable(null);
        }
    }

    public Integer findByReplyId(Integer replyId) {
        return em.createQuery(
                        "SELECT COUNT(brl) FROM BoardReplyLike brl WHERE brl.boardReply.id = :replyId",
                        Long.class)
                .setParameter("replyId", replyId)
                .getSingleResult()
                .intValue();
    }

    public void save(BoardReplyLike boardReplyLike) {
        em.persist(boardReplyLike);
    }
}
