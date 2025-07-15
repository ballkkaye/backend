package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
public class TodayGameControllerTest extends MyRestDoc {

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
    void get_today_game_predictions_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/today-games/prediction")
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())

                // Game 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameId").value(423))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamName").value("SSG 랜더스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homePitcherName").value("콜어빈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayPitcherName").value("김건우"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55257.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homePredictionScore").value(4.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayPredictionScore").value(3.9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].totalPredictionScore").value(8.1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeWinPercent").value(52.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayWinPercent").value(48.0))

                // Game 2
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameId").value(424))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeamName").value("삼성 라이온즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeamName").value("한화 이글스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homePitcherName").value("가라비토"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayPitcherName").value("문동주"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55460.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/52701.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homePredictionScore").value(3.7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayPredictionScore").value(3.8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].totalPredictionScore").value(7.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeWinPercent").value(49.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayWinPercent").value(50.8))

                // Game 3
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameId").value(425))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeamName").value("NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeamName").value("롯데 자이언츠"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homePitcherName").value("최성영"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayPitcherName").value("감보아"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/66920.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/55532.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homePredictionScore").value(4.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayPredictionScore").value(3.6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].totalPredictionScore").value(8.1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeWinPercent").value(55.6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayWinPercent").value(44.4))

                // Game 4
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameId").value(426))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeamName").value("KT 위즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homePitcherName").value("소형준"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayPitcherName").value("임찬규"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/50030.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/61101.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homePredictionScore").value(3.8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayPredictionScore").value(3.8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].totalPredictionScore").value(7.6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeWinPercent").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayWinPercent").value(50.0))

                // Game 5
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameId").value(427))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeamName").value("키움 히어로즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeamName").value("KIA 타이거즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homePitcherName").value("하영민"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayPitcherName").value("김건국"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homePitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/64350.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayPitcherProfileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/76225.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homePredictionScore").value(3.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayPredictionScore").value(3.9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].totalPredictionScore").value(7.4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeWinPercent").value(47.6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayWinPercent").value(52.4))

                // Games with null values
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].homePitcherName").value(Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].awayPitcherName").value(Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].homePitcherProfileUrl").value(Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].awayPitcherProfileUrl").value(Matchers.nullValue()));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_today_games_test() throws Exception {
        // given
        String date = "2025-07-15";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/today-games")
                        .param("date", date)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())

                // Game 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameId").value(423))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameStatus").value("SCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("잠실야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].broadcastChannel").value("MS_T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homePitcherName").value("콜어빈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamLogoUrl").value("/img/logo/두산로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayPitcherName").value("김건우"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamLogoUrl").value("/img/logo/SSG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].ticketLink").value("https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB004"))

                // Game 2
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameId").value(424))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameStatus").value("SCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].stadiumName").value("대구 삼성라이온즈파크"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].broadcastChannel").value("KN_T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homePitcherName").value("가라비토"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeamLogoUrl").value("/img/logo/삼성로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayPitcherName").value("문동주"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeamLogoUrl").value("/img/logo/한화로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].ticketLink").value("https://www.ticketlink.co.kr/sports#reservation"))

                // Game 3
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameId").value(425))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameStatus").value("SCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].stadiumName").value("창원 NC파크"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].broadcastChannel").value("SS_T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homePitcherName").value("최성영"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeamLogoUrl").value("/img/logo/NC로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayPitcherName").value("감보아"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeamLogoUrl").value("/img/logo/롯데로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].ticketLink").value("https://ticket.ncdinos.com/login"))

                // Game 4
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameId").value(426))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameStatus").value("SCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].stadiumName").value("수원 KT위즈파크"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].broadcastChannel").value("SPO_T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homePitcherName").value("소형준"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeamLogoUrl").value("/img/logo/KT로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayPitcherName").value("임찬규"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeamLogoUrl").value("/img/logo/LG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].ticketLink").value("https://www.ktwiz.co.kr/ticket/reservation"))

                // Game 5
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameId").value(427))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameStatus").value("SCHEDULED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].stadiumName").value("고척스카이돔"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].broadcastChannel").value("SPO_2T"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homePitcherName").value("하영민"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeamLogoUrl").value("/img/logo/키움로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayPitcherName").value("김건국"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeamLogoUrl").value("/img/logo/KIA로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].ticketLink").value("https://ticket.interpark.com/Contents/Sports/GoodsInfo?SportsCode=07001&TeamCode=PB003"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
