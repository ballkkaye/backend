package com.example.ballkkaye.team;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TeamRepository {
    private final EntityManager em;

    public Optional<Team> findById(Integer teamId) {
        return Optional.ofNullable(em.find(Team.class, teamId));
    }
}
