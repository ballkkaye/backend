package com.example.ballkkaye.team.record.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodayTeamRecordRepository {
    private final EntityManager em;

    public List<TodayTeamRecord> findAll() {
        String q = "SELECT tr FROM TodayTeamRecord tr";
        return em.createQuery(q, TodayTeamRecord.class).getResultList();
    }
}
