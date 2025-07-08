package com.example.ballkkaye.player.hitterLineup.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodayHitterLineupRepository {
    private final EntityManager em;


    /**
     * 오늘 경기 기준 타자 라인업 조회
     */
    public List<TodayHitterLineup> findByGameIdAndTeamId(Integer gameId, Integer teamId) {
        return em.createQuery("""
                            SELECT h FROM TodayHitterLineup h
                            WHERE h.game.id = :gameId
                              AND h.team.id = :teamId
                            ORDER BY h.todayHitterOrder
                        """, TodayHitterLineup.class)
                .setParameter("gameId", gameId)
                .setParameter("teamId", teamId)
                .getResultList();
    }
}

