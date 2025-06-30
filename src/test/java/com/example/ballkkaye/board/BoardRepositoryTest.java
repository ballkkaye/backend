package com.example.ballkkaye.board;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.StadiumType;
import com.example.ballkkaye.common.enums.UserRole;
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

}
