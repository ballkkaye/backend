package com.example.ballkkaye.board.image;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

@Import(BoardImageRepository.class)
@DataJpaTest
public class BoardRepositoryImageTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private BoardImageRepository boardImageRepository;

    @Test
    public void save_test() {
        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team team = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(team);

        User user = User.builder()
                .email("save_test@nate.com")
                .name("이미지저장테스트")
                .nickname("saveTest")
                .password("1234")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .profileUrl("/img/profile.png")
                .userRole(UserRole.USER)
                .providerType(ProviderType.GOOGLE)
                .username("ssar_test")
                .predictionTier(PredictionTier.IRON)
                .predictionScore(1)
                .phoneNumber("010-1234-5678")
                .team(team)
                .build();
        em.persist(user);

        Board board = Board.builder()
                .title("이미지 저장용 게시글")
                .content("이미지 저장 테스트입니다.")
                .user(user)
                .team(team)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(board);

        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .imgUrl("/img/test-save.png")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        // when
        boardImageRepository.save(boardImage);
        em.flush();
        em.clear();

        // then
        BoardImage imagePS = em.find(BoardImage.class, boardImage.getId());

        // eye
        System.out.println("===== 저장된 이미지 정보 =====");
        System.out.println("ID: " + imagePS.getId());
        System.out.println("URL: " + imagePS.getImgUrl());
        System.out.println("삭제 상태: " + imagePS.getDeleteStatus());
    }


    @Test
    public void findByBoardAndDeleteStatus_test() {
        // given
        Stadium stadium = Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build();
        em.persist(stadium);

        Team team = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(team);

        User user = User.builder()
                .email("find_test@nate.com")
                .name("이미지찾기테스트")
                .nickname("findTest")
                .password("1234")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .profileUrl("/img/profile.png")
                .userRole(UserRole.USER)
                .providerType(ProviderType.GOOGLE)
                .username("ssar_test")
                .predictionTier(PredictionTier.IRON)
                .predictionScore(1)
                .phoneNumber("010-1234-5678")
                .team(team)
                .build();
        em.persist(user);

        Board board = Board.builder()
                .title("이미지 찾기용 게시글")
                .content("이미지 조회 테스트입니다.")
                .user(user)
                .team(team)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(board);

        BoardImage img1 = BoardImage.builder()
                .board(board)
                .imgUrl("/img/test1.png")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        BoardImage img2 = BoardImage.builder()
                .board(board)
                .imgUrl("/img/test2.png")
                .deleteStatus(DeleteStatus.DELETED)
                .build();
        em.persist(img1);
        em.persist(img2);

        em.flush();
        em.clear();

        // when
        List<BoardImage> imageList = boardImageRepository.findByBoardIdAndDeleteStatus(board, DeleteStatus.NOT_DELETED);

        // eye
        System.out.println("===== 조회된 이미지 목록 (NOT_DELETED) =====");
        for (BoardImage img : imageList) {
            System.out.println("ID: " + img.getId());
            System.out.println("URL: " + img.getImgUrl());
            System.out.println("삭제 상태: " + img.getDeleteStatus());
        }
    }

}
