package com.example.ballkkaye.board;

import com.example.ballkkaye.common.enums.DeleteStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    // 게시글 저장
    public void save(Board board) {
        em.persist(board);
    }

    // 게시글 조회
    public Optional<Board> findById(Integer boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    public List<Board> findAll(Integer teamId, int page, DeleteStatus deleteStatus) {
        String q = "SELECT b FROM Board b " +
                "JOIN FETCH b.user u " +
                "LEFT JOIN FETCH b.team t " +
                "WHERE b.deleteStatus = :deleteStatus " +
                (teamId != null ? "AND t.id = :teamId " : "") +
                "ORDER BY b.id DESC";

        Query query = em.createQuery(q, Board.class)
                .setParameter("deleteStatus", deleteStatus);

        if (teamId != null) {
            query.setParameter("teamId", teamId);
        }

        query.setFirstResult(page * 5);
        query.setMaxResults(5);

        return query.getResultList();
    }


    // 최신 게시글 5개 조회 (삭제되지 않은 글만)
    public List<Board> findLatest5() {
        return em.createQuery("""
                            SELECT b FROM Board b
                            JOIN FETCH b.user u
                            LEFT JOIN FETCH b.team t
                            WHERE b.deleteStatus = 'NOT_DELETED'
                            ORDER BY b.createdAt DESC
                        """, Board.class)
                .setMaxResults(5)
                .getResultList();
    }
}
