package com.example.ballkkaye.game;

import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class GameRepository {
    private final EntityManager em;

    // 경기 찾기
    public Optional<Game> findById(Integer id) {
        Game game = em.find(Game.class, id);
        return Optional.ofNullable(game);
    }

    // 종료된 경기 찾기
    public List<Game> findCompletedGames() {
        String jpql = "SELECT g FROM Game g WHERE g.gameStatus = 'COMPLETED' AND g.homeResultScore IS NOT NULL AND g.awayResultScore IS NOT NULL";
        TypedQuery<Game> query = em.createQuery(jpql, Game.class);
        return query.getResultList();
    }

    public Game findByCompositeKey(Timestamp gameTime, Team homeTeam, Team awayTeam, Stadium stadium) {
        return em.createQuery(
                        "select g from Game g " +
                                "where g.gameTime = :gameTime and g.homeTeam = :homeTeam and g.awayTeam = :awayTeam and g.stadium = :stadium",
                        Game.class)
                .setParameter("gameTime", gameTime)
                .setParameter("homeTeam", homeTeam)
                .setParameter("awayTeam", awayTeam)
                .setParameter("stadium", stadium)
                .getSingleResult();
    }
}