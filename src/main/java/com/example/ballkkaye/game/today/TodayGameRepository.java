package com.example.ballkkaye.game.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TodayGameRepository {

    private final EntityManager em;

    public void save(List<TodayGame> games) {
        for (TodayGame game : games) {
            em.persist(game);
        }
    }

    public Optional<TodayGame> findById(Integer id) {
        TodayGame todayGame = em.find(TodayGame.class, id);
        return Optional.ofNullable(todayGame);
    }

    public List<TodayGameResponse.PredictionDTO> findTodayGameWithPitcherInfo() {
        List<Object[]> results = em.createQuery("""
                SELECT 
                    tg.game.id,
                    homeTeam.teamName,
                    awayTeam.teamName,
                    homePitcher.player.name,
                    awayPitcher.player.name,
                    homePitcher.profileUrl,
                    awayPitcher.profileUrl,
                    tg.homePredictionScore,
                    tg.awayPredictionScore,
                    tg.totalPredictionScore,
                    tg.homeWinPer,
                    tg.awayWinPer
                FROM TodayGame tg
                JOIN tg.homeTeam homeTeam
                JOIN tg.awayTeam awayTeam
                LEFT JOIN TodayStartingPitcher homePitcher
                    ON homePitcher.game.id = tg.game.id
                    AND homePitcher.player.team.id = homeTeam.id
                LEFT JOIN TodayStartingPitcher awayPitcher
                    ON awayPitcher.game.id = tg.game.id
                    AND awayPitcher.player.team.id = awayTeam.id
                """, Object[].class).getResultList();

        List<TodayGameResponse.PredictionDTO> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            TodayGameResponse.PredictionDTO dto = new TodayGameResponse.PredictionDTO(
                    (Integer) row[0], // gameId
                    (String) row[1],  // homeTeamName
                    (String) row[2],  // awayTeamName
                    (String) row[3],  // homePitcherName
                    (String) row[4],  // awayPitcherName
                    (String) row[5],  // homePitcherProfileUrl
                    (String) row[6],  // awayPitcherProfileUrl
                    round((Double) row[7]), // homePredictionScore
                    round((Double) row[8]), // awayPredictionScore
                    round((Double) row[9]), // totalPredictionScore
                    round((Double) row[10]), // homeWinPercent
                    round((Double) row[11])  // awayWinPercent
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    private double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10) / 10.0;
    }

    public Optional<TodayGame> findByGameId(Integer gameId) {
        return em.createQuery(
                        "SELECT tg FROM TodayGame tg WHERE tg.game.id = :gameId", TodayGame.class)
                .setParameter("gameId", gameId)
                .getResultStream()
                .findFirst();
    }

    public List<TodayGameResponse.ItemDTO> findTodayGameList(LocalDate date) {
        String sql = """
                SELECT
                    g.id AS game_id,
                    g.game_status,
                    FORMATDATETIME(g.game_time, 'HH:mm') AS game_time,
                    s.stadium_name,
                    g.broadcast_channel,
                    home_pitcher.name AS home_pitcher_name,
                    home_pitcher.profile_url AS home_pitcher_img,
                    away_pitcher.name AS away_pitcher_name,
                    away_pitcher.profile_url AS away_pitcher_img,
                    t.ticket_link AS ticket_link
                FROM game_tb g
                JOIN stadium_tb s ON g.stadium_id = s.id
                LEFT JOIN (
                    SELECT tsp.game_id, p.name, tsp.profile_url
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON p.id = tsp.player_id
                    WHERE p.team_id IN (
                        SELECT g2.home_team_id FROM game_tb g2 WHERE g2.id = tsp.game_id
                    )
                ) home_pitcher ON home_pitcher.game_id = g.id
                LEFT JOIN (
                    SELECT tsp.game_id, p.name, tsp.profile_url
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON p.id = tsp.player_id
                    WHERE p.team_id IN (
                        SELECT g2.away_team_id FROM game_tb g2 WHERE g2.id = tsp.game_id
                    )
                ) away_pitcher ON away_pitcher.game_id = g.id
                LEFT JOIN team_tb t ON t.id = g.home_team_id
                WHERE g.game_time >= :start AND g.game_time < :end
                ORDER BY g.game_time ASC
                """;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return rows.stream()
                .map(row -> new TodayGameResponse.ItemDTO(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (String) row[5],
                        (String) row[6],
                        (String) row[7],
                        (String) row[8],
                        (String) row[9]
                ))
                .toList();
    }
}
