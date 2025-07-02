package com.example.ballkkaye.game.today;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                    tg.id,
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

}
