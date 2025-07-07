package com.example.ballkkaye.game;

import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<Game> todayGame(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return em.createQuery("select g from Game g where g.gameTime >= :start and g.gameTime < :end", Game.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Game> findByDate(String date) {
        StringBuilder sql = new StringBuilder("SELECT * FROM game_tb");

        if (date != null && !date.isBlank()) {
            int dateLength = date.length();
            if (dateLength == 7) {
//                sql.append(" WHERE DATE_FORMAT(game_time, '%Y-%m') = :date");
                sql.append(" WHERE TO_CHAR(game_time, 'yyyy-MM') = :date");
            } else if (dateLength == 10) {
//                sql.append(" WHERE DATE_FORMAT(game_time, '%Y-%m-%d') = :date");
                sql.append(" WHERE TO_CHAR(game_time, 'yyyy-MM-DD') = :date");
            }
        }

        Query query = em.createNativeQuery(sql.toString(), Game.class);

        if (date != null && !date.isBlank() && (date.length() == 7 || date.length() == 10)) {
            query.setParameter("date", date);
        }

        return query.getResultList();
    }

    public List<String> findDistinctDatesByMonth(String month) {
        String sql = "SELECT DISTINCT FORMATDATETIME(game_time, 'yyyy-MM-dd') FROM game_tb " +
                "WHERE FORMATDATETIME(game_time, 'yyyy-MM') = ?1";
        return em.createNativeQuery(sql).setParameter(1, month).getResultList();
    }

    public List<String> findAllDistinctGameDates() {
        String sql = "SELECT DISTINCT FORMATDATETIME(game_time, 'yyyy-MM-dd') FROM game_tb";
        return em.createNativeQuery(sql).getResultList();
    }
}