package com.example.ballkkaye.board;

import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

@Import({BoardRepository.class, UserRepository.class})
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

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

        Team team = Team.builder()
                .teamName("LG 트윈스 테스트")
                .stadium1(stadium)
                .logoUrl("/img/logo/lg.png")
                .build();
        em.persist(team);

        User user = User.builder()
                .email("ssar_test@nate.com")
                .name("쌀테스트")
                .nickname("ssar_test")
                .password("1234")
                .birthDate(LocalDate.of(1999, 9, 9))
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
                .title("게시글 테스트 제목")
                .content("테스트 내용입니다.")
                .user(user)
                .team(team)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        // when
        boardRepository.save(board);

        // eye
        Board boardPS = em.find(Board.class, board.getId());
        System.out.println("===== 저장된 게시글 정보 =====");
        System.out.println("ID: " + boardPS.getId());
        System.out.println("제목: " + boardPS.getTitle());
        System.out.println("내용: " + boardPS.getContent());
        System.out.println("작성자: " + boardPS.getUser().getName());
        System.out.println("팀: " + boardPS.getTeam().getTeamName());
        System.out.println("삭제 상태: " + boardPS.getDeleteStatus());
        System.out.println("작성일: " + boardPS.getCreatedAt());
    }

    @Test
    public void find_by_id_test() {
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
                .name("찾기테스트")
                .nickname("find_test")
                .password("5678")
                .birthDate(LocalDate.of(2000, 1, 1))
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
                .title("찾기 테스트 제목")
                .content("찾기 테스트 내용")
                .user(user)
                .team(team)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        em.persist(board);

        em.flush();
        em.clear();

        // when
        Optional<Board> boardOP = boardRepository.findById(board.getId());

        // eye
        System.out.println("===== 조회된 게시글 정보 =====");
        boardOP.ifPresent(b -> {
            System.out.println("ID: " + b.getId());
            System.out.println("제목: " + b.getTitle());
            System.out.println("내용: " + b.getContent());
            System.out.println("작성자: " + b.getUser().getName());
            System.out.println("팀: " + b.getTeam().getTeamName());
            System.out.println("삭제 상태: " + b.getDeleteStatus());
            System.out.println("작성일: " + b.getCreatedAt());
        });
    }

}
