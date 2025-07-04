package com.example.ballkkaye.visitRecord;


import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Import(VisitRecordRepository.class)
@DataJpaTest
public class VisitRecordRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    VisitRecordRepository visitRecordRepository;


    @Test
    public void save_test() {
        em.flush();
        em.clear();
        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team homeTeam = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(homeTeam);

        Team awayTeam = Team.builder()
                .teamName("롯데 자이언츠 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(awayTeam);

        Game game = Game.builder()
                .stadium(stadium)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeWinPer(55.0)
                .awayWinPer(45.0)
                .homeResultScore(3)
                .awayResultScore(2)
                .gameTime(Timestamp.valueOf("2025-07-03 18:30:00"))
                .build();
        em.persist(game);

        User user = User.builder()
                .username("ssarssar123")
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .phoneNumber("01012341234")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .profileUrl("/img/profile/default.png")
                .team(homeTeam)
                .build();
        em.persist(user);

        VisitRecord visitRecord = VisitRecord.builder()
                .game(game)
                .team(homeTeam)
                .user(user)
                .result(Result.WIN)
                .content("직관 너무 재밌었어요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);


        // when
        visitRecordRepository.save(visitRecord);

        // eye
        VisitRecord vrPS = em.find(VisitRecord.class, visitRecord.getId());
        System.out.println("===== 저장된 직관기록 정보 =====");
        System.out.println("ID: " + vrPS.getId());
        System.out.println("경기 날짜: " + vrPS.getGame().getGameTime());
        System.out.println("구장 이름: " + vrPS.getGame().getStadium().getStadiumName());
        System.out.println("홈팀: " + vrPS.getGame().getHomeTeam().getTeamName());
        System.out.println("어웨이팀: " + vrPS.getGame().getAwayTeam().getTeamName());
        System.out.println("홈 점수: " + vrPS.getGame().getHomeResultScore());
        System.out.println("어웨이 점수: " + vrPS.getGame().getAwayResultScore());
        System.out.println("작성자: " + vrPS.getUser().getName());
        System.out.println("응원팀: " + vrPS.getTeam().getTeamName());
        System.out.println("결과: " + vrPS.getResult().getValue());
        System.out.println("내용: " + vrPS.getContent());
        System.out.println("삭제 상태: " + vrPS.getDeleteStatus().getLabel());
        System.out.println("작성일: " + vrPS.getCreatedAt());

    }

    @Test
    public void findByIdAndUserId_test() {
        em.flush();
        em.clear();

        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team homeTeam = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(homeTeam);

        Team awayTeam = Team.builder()
                .teamName("롯데 자이언츠 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(awayTeam);

        Game game = Game.builder()
                .stadium(stadium)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeWinPer(55.0)
                .awayWinPer(45.0)
                .homeResultScore(3)
                .awayResultScore(2)
                .gameTime(Timestamp.valueOf("2025-07-03 18:30:00"))
                .build();
        em.persist(game);

        User user = User.builder()
                .username("ssarssar123")
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .phoneNumber("01012341234")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .profileUrl("/img/profile/default.png")
                .team(homeTeam)
                .build();
        em.persist(user);

        VisitRecord visitRecord = VisitRecord.builder()
                .game(game)
                .team(homeTeam)
                .user(user)
                .result(Result.WIN)
                .content("직관 너무 재밌었어요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);

        // when
        Optional<VisitRecord> result = visitRecordRepository.findByIdAndUserId(visitRecord.getId(), user.getId());

        // eye
        VisitRecord vrPS = result.get();
        System.out.println("===== 저장된 직관기록 정보 =====");
        System.out.println("ID: " + vrPS.getId());
        System.out.println("경기 날짜: " + vrPS.getGame().getGameTime());
        System.out.println("구장 이름: " + vrPS.getGame().getStadium().getStadiumName());
        System.out.println("홈팀: " + vrPS.getGame().getHomeTeam().getTeamName());
        System.out.println("어웨이팀: " + vrPS.getGame().getAwayTeam().getTeamName());
        System.out.println("홈 점수: " + vrPS.getGame().getHomeResultScore());
        System.out.println("어웨이 점수: " + vrPS.getGame().getAwayResultScore());
        System.out.println("작성자: " + vrPS.getUser().getName());
        System.out.println("응원팀: " + vrPS.getTeam().getTeamName());
        System.out.println("결과: " + vrPS.getResult().getValue());
        System.out.println("내용: " + vrPS.getContent());
        System.out.println("삭제 상태: " + vrPS.getDeleteStatus().getLabel());
        System.out.println("작성일: " + vrPS.getCreatedAt());
    }

    @Test
    public void findAllByUserIdAndMonth_test() {
        em.flush();
        em.clear();

        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team homeTeam = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(homeTeam);

        Team awayTeam = Team.builder()
                .teamName("롯데 자이언츠 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(awayTeam);

        Game game = Game.builder()
                .stadium(stadium)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeWinPer(55.0)
                .awayWinPer(45.0)
                .homeResultScore(3)
                .awayResultScore(2)
                .gameTime(Timestamp.valueOf("2025-07-03 18:30:00"))
                .build();
        em.persist(game);

        User user = User.builder()
                .username("ssarssar123")
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .phoneNumber("01012341234")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .profileUrl("/img/profile/default.png")
                .team(homeTeam)
                .build();
        em.persist(user);

        VisitRecord visitRecord = VisitRecord.builder()
                .game(game)
                .team(homeTeam)
                .user(user)
                .result(Result.WIN)
                .content("직관 너무 재밌었어요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);

        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 7, 31);

        // when
        List<VisitRecord> result = visitRecordRepository.findAllByUserIdAndMonth(user.getId(), start, end);

        // eye
        VisitRecord vrPS = result.get(0);
        System.out.println("===== 저장된 직관기록 정보 =====");
        System.out.println("ID: " + vrPS.getId());
        System.out.println("경기 날짜: " + vrPS.getGame().getGameTime());
        System.out.println("구장 이름: " + vrPS.getGame().getStadium().getStadiumName());
        System.out.println("홈팀: " + vrPS.getGame().getHomeTeam().getTeamName());
        System.out.println("어웨이팀: " + vrPS.getGame().getAwayTeam().getTeamName());
        System.out.println("홈 점수: " + vrPS.getGame().getHomeResultScore());
        System.out.println("어웨이 점수: " + vrPS.getGame().getAwayResultScore());
        System.out.println("작성자: " + vrPS.getUser().getName());
        System.out.println("응원팀: " + vrPS.getTeam().getTeamName());
        System.out.println("결과: " + vrPS.getResult().getValue());
        System.out.println("내용: " + vrPS.getContent());
        System.out.println("삭제 상태: " + vrPS.getDeleteStatus().getLabel());
        System.out.println("작성일: " + vrPS.getCreatedAt());

    }


    @Test
    public void findAllByUserIdAndDate_test() {
        em.flush();
        em.clear();

        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team homeTeam = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(homeTeam);

        Team awayTeam = Team.builder()
                .teamName("롯데 자이언츠 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(awayTeam);

        Game game = Game.builder()
                .stadium(stadium)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeWinPer(55.0)
                .awayWinPer(45.0)
                .homeResultScore(3)
                .awayResultScore(2)
                .gameTime(Timestamp.valueOf("2025-07-03 18:30:00"))
                .build();
        em.persist(game);

        User user = User.builder()
                .username("ssarssar123")
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .phoneNumber("01012341234")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .profileUrl("/img/profile/default.png")
                .team(homeTeam)
                .build();
        em.persist(user);

        VisitRecord visitRecord = VisitRecord.builder()
                .game(game)
                .team(homeTeam)
                .user(user)
                .result(Result.WIN)
                .content("직관 너무 재밌었어요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);

        // when
        List<VisitRecord> result = visitRecordRepository.findAllByUserIdAndDate(user.getId(), LocalDate.of(2025, 7, 4));

        // eye
        VisitRecord vrPS = result.get(0);
        System.out.println("===== 저장된 직관기록 정보 =====");
        System.out.println("ID: " + vrPS.getId());
        System.out.println("경기 날짜: " + vrPS.getGame().getGameTime());
        System.out.println("구장 이름: " + vrPS.getGame().getStadium().getStadiumName());
        System.out.println("홈팀: " + vrPS.getGame().getHomeTeam().getTeamName());
        System.out.println("어웨이팀: " + vrPS.getGame().getAwayTeam().getTeamName());
        System.out.println("홈 점수: " + vrPS.getGame().getHomeResultScore());
        System.out.println("어웨이 점수: " + vrPS.getGame().getAwayResultScore());
        System.out.println("작성자: " + vrPS.getUser().getName());
        System.out.println("응원팀: " + vrPS.getTeam().getTeamName());
        System.out.println("결과: " + vrPS.getResult().getValue());
        System.out.println("내용: " + vrPS.getContent());
        System.out.println("삭제 상태: " + vrPS.getDeleteStatus().getLabel());
        System.out.println("작성일: " + vrPS.getCreatedAt());


    }


    @Test
    public void findDistinctDatesByUserIdAndMonth_test() {
        em.flush();
        em.clear();

        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team homeTeam = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(homeTeam);

        Team awayTeam = Team.builder()
                .teamName("롯데 자이언츠 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(awayTeam);

        Game game = Game.builder()
                .stadium(stadium)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .homeWinPer(55.0)
                .awayWinPer(45.0)
                .homeResultScore(3)
                .awayResultScore(2)
                .gameTime(Timestamp.valueOf("2025-07-03 18:30:00"))
                .build();
        em.persist(game);

        User user = User.builder()
                .username("ssarssar123")
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .phoneNumber("01012341234")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .profileUrl("/img/profile/default.png")
                .team(homeTeam)
                .build();
        em.persist(user);

        VisitRecord visitRecord = VisitRecord.builder()
                .game(game)
                .team(homeTeam)
                .user(user)
                .result(Result.WIN)
                .content("직관 너무 재밌었어요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);


        Timestamp start = Timestamp.valueOf(LocalDate.of(2025, 7, 1).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.of(2025, 7, 31).plusDays(1).atStartOfDay().minusSeconds(1));


        // when
        List<Date> dates = visitRecordRepository.findDistinctDatesByUserIdAndMonth(user.getId(), start, end);

        // eye
        System.out.println("===== 하이라이트 날짜 목록 =====");
        dates.forEach(d -> System.out.println("날짜: " + d));
    }

}
