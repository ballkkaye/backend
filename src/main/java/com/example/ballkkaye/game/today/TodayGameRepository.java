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

    // 오늘 경기의 예측 관련 원본 데이터를 조회
    public List<Object[]> findTodayGamePredictionData(LocalDate today) {
        today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
        Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

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
                        WHERE tg.gameTime BETWEEN :startTimestamp AND :endTimestamp
                        """, Object[].class)
                .setParameter("startTimestamp", startTimestamp)
                .setParameter("endTimestamp", endTimestamp)
                .getResultList();
    }

    // 특정 날짜에 대한 모든 경기의 간략한 정보 조회
    public List<Object[]> findTodayGameList(LocalDate today) {
        String sql = """
                    SELECT
                        g.game_id,
                        g.game_status,
                        g.game_time,
                        s.stadium_name, -- 여기 명시적으로 구장 이름 가져오기
                        g.broadcast_channel,
                        home_pitcher.name AS home_pitcher_name,
                        home_pitcher.profile_url AS home_pitcher_img,
                        away_pitcher.name AS away_pitcher_name,
                        away_pitcher.profile_url AS away_pitcher_img,
                        t.ticket_link
                    FROM today_game_tb g
                    JOIN stadium_tb s ON g.stadium_id = s.id
                    LEFT JOIN (
                        SELECT
                            tsp.game_id,
                            p.name,
                            tsp.profile_url
                        FROM today_starting_pitcher_tb tsp
                        JOIN player_tb p ON p.id = tsp.player_id
                        JOIN game_tb og ON og.id = tsp.game_id
                        WHERE og.home_team_id = p.team_id
                    ) home_pitcher ON home_pitcher.game_id = g.game_id 
                    LEFT JOIN (
                        SELECT
                            tsp.game_id,
                            p.name,
                            tsp.profile_url
                        FROM today_starting_pitcher_tb tsp
                        JOIN player_tb p ON p.id = tsp.player_id
                        JOIN game_tb og ON og.id = tsp.game_id
                        WHERE og.away_team_id = p.team_id
                    ) away_pitcher ON away_pitcher.game_id = g.game_id
                    LEFT JOIN team_tb t ON t.id = g.home_team_id
                    WHERE g.game_time >= :start AND g.game_time < :end
                    ORDER BY g.game_id ASC
                """;

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return em.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
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

    // 오늘의 경기 저장
    public TodayGame save(TodayGame todayGame) {
        em.persist(todayGame);
        return todayGame;
    }
}
