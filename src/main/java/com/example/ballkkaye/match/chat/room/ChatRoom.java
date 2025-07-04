package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.Region;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.match.MatchRequest;
import com.example.ballkkaye.team.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "chat_room_tb")
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Gender preferredGender;

    @Column
    private Age preferredAge;

    @Column
    private Region preferredRegion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus;

    @Column(nullable = false)
    private Boolean isSameTeam;

    @Column
    private Timestamp lastDisconnectedAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public ChatRoom(Game game,
                    Team team,
                    Integer maxParticipants,
                    Gender preferredGender,
                    Age preferredAge,
                    Region preferredRegion,
                    DeleteStatus deleteStatus,
                    Boolean isSameTeam,
                    Timestamp lastDisconnectedAt) {
        this.game = game;
        this.team = team;
        this.maxParticipants = maxParticipants;
        this.preferredGender = preferredGender;
        this.preferredAge = preferredAge;
        this.preferredRegion = preferredRegion;
        this.deleteStatus = deleteStatus;
        this.isSameTeam = isSameTeam;
        this.lastDisconnectedAt = lastDisconnectedAt;
    }

    public void update(Game gamePS, Team teamPS, MatchRequest.UpdateDTO reqDTO) {
        this.game = gamePS == null ? this.game : gamePS;
        this.team = teamPS == null ? this.team : teamPS;
        this.maxParticipants = reqDTO.getMaxParticipants() == null ? this.maxParticipants : reqDTO.getMaxParticipants();
        this.preferredGender = reqDTO.getPreferredGender() == null ? this.preferredGender : reqDTO.getPreferredGender();
        this.preferredAge = reqDTO.getPreferredAge() == null ? this.preferredAge : reqDTO.getPreferredAge();
        this.preferredRegion = reqDTO.getPreferredRegion() == null ? this.preferredRegion : reqDTO.getPreferredRegion();
        this.isSameTeam = reqDTO.getIsSameTeam() == null ? this.isSameTeam : reqDTO.getIsSameTeam();
    }

    public void delete() {
        this.deleteStatus = DeleteStatus.DELETED;
    }
}