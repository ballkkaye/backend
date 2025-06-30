package com.example.ballkkaye.user;

import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.StadiumType;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.team.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

@Import({UserRepository.class})
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void find_by_id() {
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
                .email("findbyid@example.com")
                .name("홍길동")
                .nickname("hong")
                .password("1234")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .profileUrl("/img/profile.png")
                .userRole(UserRole.USER)
                .team(team)
                .build();
        em.persist(user);

        // when
        Optional<User> result = userRepository.findById(user.getId());

        // eye
        System.out.println("====== 사용자 조회 결과 ======");
        if (result.isPresent()) {
            User u = result.get();
            System.out.println("ID: " + u.getId());
            System.out.println("이름: " + u.getName());
            System.out.println("닉네임: " + u.getNickname());
            System.out.println("이메일: " + u.getEmail());
            System.out.println("팀: " + u.getTeam().getTeamName());
        } else {
            System.out.println("사용자를 찾을 수 없습니다.");
        }
    }
}

