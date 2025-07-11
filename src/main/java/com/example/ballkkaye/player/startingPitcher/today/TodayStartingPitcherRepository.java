package com.example.ballkkaye.player.startingPitcher.today;

import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.team.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodayStartingPitcherRepository {
    private final EntityManager em;

    public Double getPitcherEraByGameAndTeam(Game game, Team team) {
        return em.createQuery(
                        "SELECT t.ERA FROM TodayStartingPitcher t " +
                                "WHERE t.game = :game AND t.player.team = :team", Double.class)
                .setParameter("game", game)
                .setParameter("team", team)
                .getSingleResult();
    }


    /**
     * 특정 경기 ID와 팀 이름으로 오늘의 선발투수 조회
     */
    public List<TodayStartingPitcher> findByGameIdAndTeam(Integer gameId, String teamName) {
        return em.createQuery("""
                            SELECT t
                            FROM TodayStartingPitcher t
                            WHERE t.game.id = :gameId
                              AND t.player.team.teamName = :teamName
                        """, TodayStartingPitcher.class)
                .setParameter("gameId", gameId)
                .setParameter("teamName", teamName)
                .getResultList();
    }

    public TodayStartingPitcher save(TodayStartingPitcher todayStartingPitcher) {
        em.persist(todayStartingPitcher);
        return todayStartingPitcher;
    }
}
