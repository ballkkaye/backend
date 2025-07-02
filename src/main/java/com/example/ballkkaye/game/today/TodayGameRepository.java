package com.example.ballkkaye.game.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TodayGameRepository {

    private final EntityManager em;

    public Optional<TodayGame> findById(Integer id) {
        TodayGame todayGame = em.find(TodayGame.class, id);
        return Optional.ofNullable(todayGame);
    }
}
