package com.example.ballkkaye.team;

import com.example.ballkkaye.common.enums.StadiumType;
import com.example.ballkkaye.stadium.Stadium;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(TeamRepository.class)
@DataJpaTest
public class TeamRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void find_by_id() {
        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("고척스카이돔")
                .location("서울 구로구")
                .stadiumType(StadiumType.INDOOR)
                .build();
        em.persist(stadium);

        Team team = Team.builder()
                .teamName("키움 히어로즈 테스트")
                .logoUrl("/img/logo/kiwoom.png")
                .stadium1(stadium)
                .build();
        em.persist(team);

        em.flush();
        em.clear();

        // when
        Team t = teamRepository.findById(team.getId())
                .orElseThrow(() -> new RuntimeException("팀이 없습니다"));

        // eye
        System.out.println("====== 팀 정보 조회 ======");
        System.out.println("ID: " + t.getId());
        System.out.println("팀명: " + t.getTeamName());
        System.out.println("로고 URL: " + t.getLogoUrl());
        System.out.println("홈구장: " + t.getStadium1().getStadiumName());
    }
}
