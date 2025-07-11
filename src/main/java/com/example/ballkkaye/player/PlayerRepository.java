package com.example.ballkkaye.player;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlayerRepository {
    private final EntityManager em;


    // 선수 저장
    public Player save(Player player) {
        em.persist(player);
        return player;
    }

    public void saveAll(List<Player> players) {
        for (Player p : players) {
            em.persist(p);
        }
    }
}
