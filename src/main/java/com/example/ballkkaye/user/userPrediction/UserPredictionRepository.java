package com.example.ballkkaye.user.userPrediction;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPredictionRepository {

    private final EntityManager em;

    public UserPrediction save(UserPrediction userPrediction) {
        em.persist(userPrediction);
        return userPrediction;
    }

    public List<UserPredictionResponse.TodayGameDTO> findTodayGamesForPrediction(LocalDate today, Integer userId) {
        String sql = "SELECT\n" +
                "  g.id,\n" +
                "  g.game_time,\n" +
                "  ht.id,\n" +
                "  ht.team_name,\n" +
                "  ht.logo_url,\n" +
                "  at.id,\n" +
                "  at.team_name,\n" +
                "  at.logo_url,\n" +
                "  (SELECT COUNT(*) FROM user_prediction_tb up1 WHERE up1.game_id = g.id AND up1.team_id = g.home_team_id),\n" +
                "  (SELECT COUNT(*) FROM user_prediction_tb up2 WHERE up2.game_id = g.id AND up2.team_id = g.away_team_id)\n" +
                "FROM today_game_tb g\n" +
                "JOIN team_tb ht ON g.home_team_id = ht.id\n" +
                "JOIN team_tb at ON g.away_team_id = at.id\n" +
                "WHERE CAST(g.game_time AS DATE) = :today\n" +
                "  AND g.game_time > CURRENT_TIMESTAMP\n" +
                "  AND g.id NOT IN (\n" +
                "      SELECT game_id FROM user_prediction_tb WHERE user_id = :userId\n" +
                "  )\n";

        List<Object[]> results = em.createNativeQuery(sql)
                .setParameter("today", today)
                .setParameter("userId", userId)
                .getResultList();

        List<UserPredictionResponse.TodayGameDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            UserPredictionResponse.TodayGameDTO dto = new UserPredictionResponse.TodayGameDTO();
            dto.setGameId((Integer) row[0]);
            Timestamp gameTime = (Timestamp) row[1];
            dto.setGameTime(gameTime.toLocalDateTime().toLocalTime().toString()); // or substring(0, 5)

            UserPredictionResponse.TeamDTO home = new UserPredictionResponse.TeamDTO();
            home.setTeamId((Integer) row[2]);
            home.setTeamName((String) row[3]);
            home.setLogoUrl((String) row[4]);

            UserPredictionResponse.TeamDTO away = new UserPredictionResponse.TeamDTO();
            away.setTeamId((Integer) row[5]);
            away.setTeamName((String) row[6]);
            away.setLogoUrl((String) row[7]);

            Integer homeVotes = ((Number) row[8]).intValue();
            Integer awayVotes = ((Number) row[9]).intValue();
            dto.setHomeTeam(home);
            dto.setAwayTeam(away);
            dto.setHomeVoteRate(homeVotes); // 비율 아님, raw count
            dto.setAwayVoteRate(awayVotes); // 비율 아님, raw count

            dtos.add(dto);
        }
        return dtos;
    }
}
