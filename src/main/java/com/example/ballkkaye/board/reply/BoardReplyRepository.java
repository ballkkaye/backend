package com.example.ballkkaye.board.reply;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardReplyRepository {
    private final EntityManager em;

    public Integer totalCount(Integer boardId) {
        String q = "SELECT COUNT(c) FROM BoardReply c WHERE c.board.id = :boardId";
        return em.createQuery(q, Long.class)
                .setParameter("boardId", boardId)
                .getSingleResult().intValue();
    }

    public List<BoardReply> findByBoardId(Integer boardId) {
        return em.createQuery("SELECT br FROM BoardReply br WHERE br.board.id = :boardId", BoardReply.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    public List<BoardReply> findByBoardIdAndParentReplyId(Integer boardId, Integer parentReplyId) {
        if (parentReplyId == null) {
            // 부모 댓글이 없는 댓글만 조회 (parentReplyId IS NULL)
            return em.createQuery(
                            "SELECT br FROM BoardReply br WHERE br.board.id = :boardId AND br.parentReplyId IS NULL",
                            BoardReply.class)
                    .setParameter("boardId", boardId)
                    .getResultList();
        } else {
            // 특정 부모 댓글에 대한 자식 댓글 조회
            return em.createQuery(
                            "SELECT br FROM BoardReply br WHERE br.board.id = :boardId AND br.parentReplyId.id = :parentReplyId",
                            BoardReply.class)
                    .setParameter("boardId", boardId)
                    .setParameter("parentReplyId", parentReplyId) // 실제 BoardReply 객체를 전달
                    .getResultList();
        }
    }

    // 게시글 댓글 조회
    public Optional<BoardReply> findById(Integer replyId) {
        return Optional.ofNullable(em.find(BoardReply.class, replyId));
    }

    public void save(BoardReply boardReply) {
        em.persist(boardReply);
    }
}
