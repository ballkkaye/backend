package com.example.ballkkaye.todayGame;


import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

@Import(TodayGameRepository.class)
@DataJpaTest
public class TodayGameRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TodayGameRepository todayGameRepository;


    @Test
    public void findByStadiumIdAndDate_test() {
        // given
        Integer stadiumId = 1;
        

        // when
        Optional<TodayGame> result = todayGameRepository.findByStadiumIdAndDate(stadiumId, LocalDate.now());

        // then
        if (result.isPresent()) {
            TodayGame tg = result.get();
            System.out.println("경기 조회 성공!");
            System.out.println("구장명: " + tg.getStadium().getStadiumName());
            System.out.println("홈팀: " + tg.getHomeTeam().getTeamName());
            System.out.println("어웨이팀: " + tg.getAwayTeam().getTeamName());
            System.out.println("경기시간: " + tg.getGameTime());
        } else {
            System.out.println("해당 구장의 오늘 경기가 없습니다.");
        }
    }
}
