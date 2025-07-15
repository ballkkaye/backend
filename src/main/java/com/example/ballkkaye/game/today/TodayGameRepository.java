package com.example.ballkkaye.game.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TodayGameRepository {

    private final EntityManager em;


    // 여러 개의 TodayGame 객체를 데이터베이스에 저장
    public void save(List<TodayGame> games) {
        for (TodayGame game : games) {
            em.persist(game);
        }
    }


    // 오늘 경기의 예측 관련 원본 데이터를 조회
    public List<Object[]> findTodayGamePredictionData() {
        return em.createQuery("""
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
    }

    public List<TodayGame> findTodayGames() {
        return em.createQuery("""
                        SELECT tg FROM TodayGame tg
                        JOIN FETCH tg.homeTeam
                        JOIN FETCH tg.awayTeam
                        ORDER BY tg.id DESC
                        """, TodayGame.class)
                .setMaxResults(5)
                .getResultList();
    }

    // 특정 날짜에 대한 모든 경기의 간략한 정보 조회
    public List<TodayGameResponse.ItemDTO> findTodayGameList(LocalDate date) {
        String sql = """
                SELECT
                    g.game_id AS today_game_id,
                    g.game_status,
                    FORMATDATETIME(g.game_time, 'HH:mm') AS game_time,
                    s.stadium_name,
                    g.broadcast_channel,
                    home_pitcher.name AS home_pitcher_name,       
                    ht.logo_url AS home_team_logo_url,            
                    away_pitcher.name AS away_pitcher_name,      
                    at.logo_url AS away_team_logo_url,           
                    ht.ticket_link AS ticket_link
                FROM today_game_tb g
                JOIN stadium_tb s ON g.stadium_id = s.id
                LEFT JOIN team_tb ht ON g.home_team_id = ht.id
                LEFT JOIN team_tb at ON g.away_team_id = at.id
                LEFT JOIN (
                    SELECT tsp.game_id, p.name
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON tsp.player_id = p.id
                    JOIN game_tb gg ON tsp.game_id = gg.id
                    WHERE gg.home_team_id = p.team_id
                ) home_pitcher ON home_pitcher.game_id = g.game_id
                LEFT JOIN (
                    SELECT tsp.game_id, p.name
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON tsp.player_id = p.id
                    JOIN game_tb gg ON tsp.game_id = gg.id
                    WHERE gg.away_team_id = p.team_id
                ) away_pitcher ON away_pitcher.game_id = g.game_id
                WHERE g.game_time >= :start AND g.game_time < :end
                ORDER BY g.game_id ASC
                """;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return rows.stream()
                .map(row -> new TodayGameResponse.ItemDTO(
                        (Integer) row[0], // game_id
                        (String) row[1],  // game_status
                        (String) row[2],  // game_time
                        (String) row[3],  // stadium_name
                        (String) row[4],  // broadcast_channel
                        (String) row[5],  // home_pitcher_name
                        (String) row[6],  // home_team_logo
                        (String) row[7],  // away_pitcher_name
                        (String) row[8],  // away_team_logo
                        (String) row[9]   // ticket_link
                ))
                .toList();
    }

    /**
     * 특정 구장의 오늘 날짜 경기를 조회
     * - stadiumId와 날짜(LocalDate)로 필터링
     * - 예외 발생 시 Optional.empty() 반환
     */
    public Optional<TodayGame> findByStadiumIdAndDate(Integer stadiumId, LocalDate date) {
        try {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            return em.createQuery("""
                                SELECT tg FROM TodayGame tg
                                WHERE tg.stadium.id = :stadiumId
                                  AND tg.gameTime >= :start
                                  AND tg.gameTime < :end
                            """, TodayGame.class)
                    .setParameter("stadiumId", stadiumId)
                    .setParameter("start", Timestamp.valueOf(start))
                    .setParameter("end", Timestamp.valueOf(end))
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}