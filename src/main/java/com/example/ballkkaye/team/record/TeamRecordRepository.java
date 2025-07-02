package com.example.ballkkaye.team.record;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamRecordRepository {
    private final EntityManager em;

    public Integer getRank(Integer teamId) {
        String q = "SELECT tr.teamRank FROM TeamRecord tr WHERE tr.team.id = :teamId";
        return em.createQuery(q, Integer.class)
                .setParameter("teamId", teamId)
                .getSingleResult();
    }
}
