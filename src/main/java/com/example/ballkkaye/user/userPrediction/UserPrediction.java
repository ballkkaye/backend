package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.PredictionStatus;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "user_prediction_tb")
@Entity
public class UserPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private TodayGame game;

    @ManyToOne(fetch = FetchType.LAZY) // 무승부를 예측할 경우 선택한 팀 없음
    @JoinColumn(name = "team_id")
    private Team userChoiceTeam;

    @Column
    @Enumerated(EnumType.STRING)
    private PredictionStatus result;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public UserPrediction(User user, TodayGame game, Team userChoiceTeam, PredictionStatus result) {
        this.user = user;
        this.game = game;
        this.userChoiceTeam = userChoiceTeam;
        this.result = result;
    }
}