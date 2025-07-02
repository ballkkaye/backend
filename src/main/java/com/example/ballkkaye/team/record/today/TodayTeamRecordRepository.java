package com.example.ballkkaye.team.record.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodayTeamRecordRepository {
    private final EntityManager em;

    public Double getLeagueAverageOps() {
        return em.createQuery("SELECT AVG(t.OPS) FROM TodayTeamRecord t", Double.class)
                .getSingleResult();
    }

    public Double getLeagueAverageR() {
        return em.createQuery("SELECT AVG(t.R) FROM TodayTeamRecord t", Double.class)
                .getSingleResult();
    }

    public Double getLeagueAverageEra() {
        return em.createQuery("SELECT AVG(t.ERA) FROM TodayTeamRecord t", Double.class)
                .getSingleResult();
    }

    public Double getAverageGameCount() {
        return em.createQuery("SELECT AVG(t.totalGame) FROM TodayTeamRecord t", Double.class)
                .getSingleResult();
    }

    public List<TodayTeamRecord> findAll() {
        return em.createQuery("SELECT t FROM TodayTeamRecord t", TodayTeamRecord.class)
                .getResultList();
    }
}
