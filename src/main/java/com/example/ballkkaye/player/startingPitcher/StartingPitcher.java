package com.example.ballkkaye.player.startingPitcher;

import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "starting_pitcher_tb")
@Entity
public class StartingPitcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Player player;

    @Column
    private String profileUrl;

    @Column(nullable = true)
    private Double ERA;

    @Column(nullable = true)
    private Integer gameCount;

    @Column(nullable = true)
    private String result;

    @Column(nullable = true)
    private Integer QS;

    @Column(nullable = true)
    private Double WHIP;

    @CreationTimestamp
    private Timestamp createdAt;
}
