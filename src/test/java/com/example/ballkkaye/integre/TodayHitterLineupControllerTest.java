package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TodayHitterLineupControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").userRole(UserRole.USER).build();
        accessToken = JwtUtil.create(ssar);
    }

    @Test
    void get_detail_matchup_test() throws Exception {
        // given
        int gameId = 423;   // 테스트용 gameId
        int teamId = 3;     // 테스트용 teamId

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/api/today/hitter-lineup")
                        .param("gameId", String.valueOf(gameId))
                        .param("teamId", String.valueOf(teamId))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameId").value(423))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.season").value(2025))

                // Hitters
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].hitPredictionPer").value(35.7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].hitterOrder").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].name").value("김도형"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].position").value("1루수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].ab").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].h").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].avg").value(0.667))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[0].ops").value(0.91))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].hitPredictionPer").value(32.3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].hitterOrder").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].name").value("서인국"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].position").value("2루수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].ab").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].h").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].avg").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[1].ops").value(0.85))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].hitPredictionPer").value(23.6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].hitterOrder").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].name").value("정경호"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].position").value("3루수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].ab").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].h").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].avg").value(0.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[2].ops").value(0.69))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].hitPredictionPer").value(28.3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].hitterOrder").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].name").value("남주혁"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].position").value("유격수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].ab").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].h").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].avg").value(0.333))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[3].ops").value(0.8))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].hitPredictionPer").value(28.9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].hitterOrder").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].name").value("이진혁"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].position").value("좌익수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].ab").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].h").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].avg").value(0.333))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[4].ops").value(0.815))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].hitPredictionPer").value(40.8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].hitterOrder").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].name").value("김요한"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].position").value("중견수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].ab").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].h").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].avg").value(0.75))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[5].ops").value(1.02))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].hitPredictionPer").value(30.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].hitterOrder").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].name").value("유승우"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].position").value("우익수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].ab").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].h").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].avg").value(0.333))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[6].ops").value(0.88))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].hitPredictionPer").value(23.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].hitterOrder").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].name").value("장도연"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].position").value("포수"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].ab").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].h").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].avg").value(0.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[7].ops").value(0.7))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].hitPredictionPer").value(35.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].hitterOrder").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].name").value("강호동"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].position").value("지명타자"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].ab").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].h").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].avg").value(0.667))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters[8].ops").value(0.95))

                // Pitcher Profile and Stats
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("콜어빈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameCount").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("5승 7패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.era").value(4.86))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.qs").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.whip").value(1.38));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
