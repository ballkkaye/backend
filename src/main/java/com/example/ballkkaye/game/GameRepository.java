package com.example.ballkkaye.game;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    // 경기 단건 저장
    public Game save(Game game) {
        em.persist(game);
        return game;
    }
}