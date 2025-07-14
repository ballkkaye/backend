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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class HomeControllerTest extends MyRestDoc {

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
    void save_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/home")
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                // todayGames 검증
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].gameId").value(423))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].gameStatus").value("경기예정"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].gameTime").value("20:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].stadiumName").value("잠실"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].broadcastChannel").value("MS_T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].homePitcherName").value("콜어빈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].homeTeamLogoUrl").value("/img/logo/두산로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].awayPitcherName").value("김건우"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].awayTeamLogoUrl").value("/img/logo/SSG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].ticketLink").value("https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB004"))

                // winPredictions 검증
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].gameId").value(423))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].homePitcherName").value("콜어빈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].awayPitcherName").value("김건우"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].homePredictionScore").value(4.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].awayPredictionScore").value(3.9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].totalPredictionScore").value(8.1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].homeWinPercent").value(52.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions[0].awayWinPercent").value(48.0))

                // boards 검증
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[1].title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[1].content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[2].title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[2].content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[3].title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[3].content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[4].title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[4].content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
