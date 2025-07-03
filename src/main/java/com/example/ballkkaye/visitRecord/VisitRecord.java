package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Result;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "visit_record_tb")
@Entity
public class VisitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Team team;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Result result;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeleteStatus deleteStatus;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public VisitRecord(Integer id, User user, Game game, Team team, Result result, String content, DeleteStatus deleteStatus, Timestamp updatedAt, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.team = team;
        this.result = result;
        this.content = content;
        this.deleteStatus = deleteStatus;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    public void delete() {
        this.deleteStatus = DeleteStatus.DELETED;
    }
}
