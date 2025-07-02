package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPredictionRepository {

    private final EntityManager em;

    public UserPrediction save(UserPrediction userPrediction) {
        em.persist(userPrediction);
        return userPrediction;
    }

    public boolean existsByUserAndGame(User user, Integer gameId) {
        String jpql = "SELECT COUNT(up) FROM UserPrediction up WHERE up.user = :user AND up.game.id = :gameId";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("user", user)
                .setParameter("gameId", gameId)
                .getSingleResult();
        return count > 0;
    }

    public List<Object[]> findTodayPredictionsByUser(Integer userId, LocalDate today) {
        String q = """
                SELECT
                    up.ID,
                    tg.ID,
                    tg.GAME_STATUS,
                    tg.HOME_RESULT_SCORE,
                    tg.AWAY_RESULT_SCORE,
                
                    ht.ID AS homeTeamId,
                    ht.TEAM_NAME AS homeTeamName,
                    ht.LOGO_URL AS homeLogoUrl,
                
                    at.ID AS awayTeamId,
                    at.TEAM_NAME AS awayTeamName,
                    at.LOGO_URL AS awayLogoUrl,
                
                    up.TEAM_ID AS userChoiceTeamId,
                    up.RESULT,
                
                    (SELECT COUNT(*)
                     FROM user_prediction_tb sub_up
                     JOIN today_game_tb sub_tg ON sub_up.GAME_ID = sub_tg.ID
                     WHERE sub_tg.ID = tg.ID AND sub_up.TEAM_ID = ht.ID) AS homeCount,
                
                    (SELECT COUNT(*)
                     FROM user_prediction_tb sub_up
                     JOIN today_game_tb sub_tg ON sub_up.GAME_ID = sub_tg.ID
                     WHERE sub_tg.ID = tg.ID AND sub_up.TEAM_ID = at.ID) AS awayCount
                
                FROM user_prediction_tb up
                JOIN today_game_tb tg ON up.GAME_ID = tg.ID
                JOIN team_tb ht ON tg.HOME_TEAM_ID = ht.ID
                JOIN team_tb at ON tg.AWAY_TEAM_ID = at.ID
                
                WHERE up.USER_ID = :userId
                  AND CAST(tg.GAME_TIME AS DATE) = :today
                
                ORDER BY tg.GAME_TIME ASC
                """;

        return em.createNativeQuery(q)
                .setParameter("userId", userId)
                .setParameter("today", today)
                .getResultList();
    }

}
