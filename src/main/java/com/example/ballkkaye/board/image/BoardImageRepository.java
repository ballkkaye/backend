package com.example.ballkkaye.board.image;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.common.enums.DeleteStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardImageRepository {
    private final EntityManager em;

    public void save(BoardImage boardImage) {
        em.persist(boardImage);
    }

    public List<BoardImage> findByBoardIdAndDeleteStatus(Board boardPS, DeleteStatus deleteStatus) {
        return em.createQuery(
                        "SELECT bi FROM BoardImage bi WHERE bi.board = :board AND bi.deleteStatus = :deleteStatus",
                        BoardImage.class)
                .setParameter("board", boardPS)
                .setParameter("deleteStatus", deleteStatus)
                .getResultList();
    }
}
