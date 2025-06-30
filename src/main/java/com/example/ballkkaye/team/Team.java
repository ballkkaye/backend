package com.example.ballkkaye.team;

import com.example.ballkkaye.stadium.Stadium;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "team_tb")
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stadium_id1", nullable = false)
    private Stadium stadium1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id2")
    private Stadium stadium2;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "ticket_link")
    private String ticketLink;

    @Builder
    public Team(Stadium stadium1, Stadium stadium2, String teamName, String logoUrl, String ticketLink) {
        this.stadium1 = stadium1;
        this.stadium2 = stadium2;
        this.teamName = teamName;
        this.logoUrl = logoUrl;
        this.ticketLink = ticketLink;
    }
}
