package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
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

    public void saveAll(List<UserPrediction> userPredictions) {
        for (UserPrediction prediction : userPredictions) {
            em.persist(prediction);
        }
    }

    public List<UserPredictionResponse.TodayGameDTO> findTodayGamesForPrediction(LocalDate today, Integer userId) {
        String sql = "SELECT\n" +
                "  g.game_id,\n" +
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
            dto.setGameTime(gameTime.toLocalDateTime().toLocalTime().toString());

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
            dto.setHomeVoteRate(homeVotes);
            dto.setAwayVoteRate(awayVotes);

            dtos.add(dto);
        }
        return dtos;
    }

    public List<UserPredictionResponse.MyPredictionDTO> findMyPredictions(Integer userId, LocalDate date) {
        String sql = """
                SELECT
                    tg.game_id AS gameId,
                    tg.game_time AS gameTime,
                
                    ht.id AS homeTeamId,
                    ht.team_name AS homeTeamName,
                    ht.logo_url AS homeTeamLogo,
                
                    at.id AS awayTeamId,
                    at.team_name AS awayTeamName,
                    at.logo_url AS awayTeamLogo,
                
                    tg.home_result_score AS homeScore,
                    tg.away_result_score AS awayScore,
                
                    up.team_id AS userChoiceTeamId,
                    tg.game_status AS gameStatus,
                    tg.updated_at AS updatedAt,
                
                    ROUND(SUM(CASE WHEN up_total.team_id = tg.home_team_id THEN 1 ELSE 0 END) * 100.0 / COUNT(up_total.game_id), 0) AS homeVoteRate,
                    ROUND(SUM(CASE WHEN up_total.team_id = tg.away_team_id THEN 1 ELSE 0 END) * 100.0 / COUNT(up_total.game_id), 0) AS awayVoteRate
                
                FROM user_prediction_tb up
                JOIN today_game_tb tg ON up.game_id = tg.id
                JOIN team_tb ht ON tg.home_team_id = ht.id
                JOIN team_tb at ON tg.away_team_id = at.id
                LEFT JOIN user_prediction_tb up_total ON up_total.game_id = tg.id
                WHERE up.user_id = :userId
                  AND tg.game_time BETWEEN :startDate AND :endDate
                GROUP BY
                    tg.id, tg.game_time,
                    ht.id, ht.team_name, ht.logo_url,
                    at.id, at.team_name, at.logo_url,
                    tg.home_result_score, tg.away_result_score,
                    up.team_id,
                    tg.game_status,
                    tg.updated_at
                    ;
                """;

        java.time.LocalDateTime startDate = date.atStartOfDay();
        java.time.LocalDateTime endDate = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Object[]> results = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        List<UserPredictionResponse.MyPredictionDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            UserPredictionResponse.MyPredictionDTO dto = new UserPredictionResponse.MyPredictionDTO();

            dto.setGameId((Integer) row[0]);

            Timestamp gameTime = (Timestamp) row[1];
            dto.setGameTime(gameTime.toLocalDateTime().toLocalTime().toString());

            UserPredictionResponse.TeamDTO homeTeam = new UserPredictionResponse.TeamDTO();
            homeTeam.setTeamId((Integer) row[2]);
            homeTeam.setTeamName((String) row[3]);
            homeTeam.setLogoUrl((String) row[4]);
            dto.setHomeTeam(homeTeam);

            UserPredictionResponse.TeamDTO awayTeam = new UserPredictionResponse.TeamDTO();
            awayTeam.setTeamId((Integer) row[5]);
            awayTeam.setTeamName((String) row[6]);
            awayTeam.setLogoUrl((String) row[7]);
            dto.setAwayTeam(awayTeam);

            dto.setHomeScore((Integer) row[8]);
            dto.setAwayScore((Integer) row[9]);

            dto.setUserChoiceTeamId((Integer) row[10]);

            String gameStatusStr = (String) row[11];
            dto.setGameStatus(GameStatus.valueOf(gameStatusStr));
            dto.setPredictionStatus(null);

            Timestamp updatedAt = (Timestamp) row[12];
            dto.setHomeVoteRate(row[13] != null ? ((Number) row[13]).intValue() : null);
            dto.setAwayVoteRate(row[14] != null ? ((Number) row[14]).intValue() : null);

            dto.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

            dtos.add(dto);
        }
        return dtos;
    }

    public boolean isExistsByUserIdAndGameId(Integer userId, Integer gameId) {
        String sql = "SELECT COUNT(*) FROM user_prediction_tb WHERE user_id = :userId AND game_id = :gameId";

        Long count = (Long) em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("gameId", gameId)
                .getSingleResult();

        return count > 0;
    }
}