package com.example.ballkkaye.team;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TeamRepository {
    private final EntityManager em;

    // 팀 조회
    public Optional<Team> findById(Integer teamId) {
        return Optional.ofNullable(em.find(Team.class, teamId));
    }

    // 전체 팀 조회
    public List<Team> findAll() {
        String q = "SELECT t FROM Team t";
        return em.createQuery(q, Team.class).getResultList();
    }

    public Team save(Team team) {
        em.persist(team);
        return team;
    }
}
