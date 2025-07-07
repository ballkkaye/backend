package com.example.ballkkaye.visitRecord.image;


import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImage;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImageRepository;
import com.example.ballkkaye.visitRecord.VisitRecord;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Import(VisitRecordImageRepository.class)
@DataJpaTest
public class VisitRecordImageRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    VisitRecordImageRepository visitRecordImageRepository;


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

        VisitRecordImage image = VisitRecordImage.builder()
                .visitRecord(visitRecord)
                .imageUrl("/upload/visit-record/sample.jpg")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(image);

        // when
        visitRecordImageRepository.save(image);


        // eye
        VisitRecordImage imagePS = em.find(VisitRecordImage.class, image.getId());
        System.out.println("===== 저장된 직관기록 이미지 정보 =====");
        System.out.println("ID: " + imagePS.getId());
        System.out.println("연결된 visitRecordId: " + imagePS.getVisitRecord().getId());
        System.out.println("이미지 URL: " + imagePS.getImageUrl());
        System.out.println("삭제 상태: " + imagePS.getDeleteStatus());
        System.out.println("업로드 시간: " + imagePS.getCreatedAt());

    }


    @Test
    public void findByVisitRecordId_test() {
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
                .content("직관은 항상 즐거워!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(visitRecord);

        VisitRecordImage image = VisitRecordImage.builder()
                .visitRecord(visitRecord)
                .imageUrl("/upload/visit-record/visit_test.jpg")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(image);

        // when
        List<VisitRecordImage> results = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecord, DeleteStatus.NOT_DELETED);

        // eye
        System.out.println("===== 직관기록 이미지 조회 결과 =====");
        for (VisitRecordImage result : results) {
            System.out.println("ID : " + result.getId());
            System.out.println("이미지 경로 : " + result.getImageUrl());
            System.out.println("삭제 상태 : " + result.getDeleteStatus());
            System.out.println("직관기록 id : " + result.getVisitRecord().getId());
        }
    }
}