package com.example.ballkkaye.game.today;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    // 주어진 gameId로 TodayGame 객체 조회
    public Optional<TodayGame> findByGameId(Integer gameId) {
        String jpql = "SELECT tg FROM TodayGame tg WHERE tg.game.id = :gameId";
        TypedQuery<TodayGame> query = em.createQuery(jpql, TodayGame.class);
        query.setParameter("gameId", gameId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
                    home_pitcher.profile_url AS home_pitcher_img,
                    away_pitcher.name AS away_pitcher_name,
                    away_pitcher.profile_url AS away_pitcher_img,
                    t.ticket_link AS ticket_link
                FROM today_game_tb g
                JOIN stadium_tb s ON g.stadium_id = s.id
                LEFT JOIN (
                    SELECT
                        tsp.game_id,
                        p.name,
                        tsp.profile_url
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON p.id = tsp.player_id
                    JOIN game_tb original_game ON original_game.id = tsp.game_id
                    WHERE original_game.home_team_id = p.team_id
                ) home_pitcher ON home_pitcher.game_id = g.game_id 
                LEFT JOIN (
                    SELECT
                        tsp.game_id,
                        p.name,
                        tsp.profile_url
                    FROM today_starting_pitcher_tb tsp
                    JOIN player_tb p ON p.id = tsp.player_id
                    JOIN game_tb original_game ON original_game.id = tsp.game_id
                    WHERE original_game.away_team_id = p.team_id
                ) away_pitcher ON away_pitcher.game_id = g.game_id
                LEFT JOIN team_tb t ON t.id = g.home_team_id
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
