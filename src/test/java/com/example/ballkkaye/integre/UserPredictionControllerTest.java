package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.userPrediction.UserPredictionRequest;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserPredictionControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String accessToken1;
    private String accessToken2;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar123").userRole(UserRole.USER).build();
        accessToken1 = JwtUtil.create(ssar);
        User cos = User.builder().id(2).username("cos123").userRole(UserRole.USER).build();
        accessToken2 = JwtUtil.create(cos);
    }

    @Test
    void today_games_test() throws Exception {
        // given
        LocalDate date = LocalDate.of(2025, 7, 16);
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/s/api/predictions/today")
                        .param("date", date.toString())
                        .header("Authorization", "Bearer " + accessToken2)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                // body[0]
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameId").value(428))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.teamId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.teamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.logoUrl").value("/img/logo/두산로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.teamName").value("SSG 랜더스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.logoUrl").value("/img/logo/SSG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayVoteRate").value(0))

                // body[1]
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameId").value(429))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.teamId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.teamName").value("삼성 라이온즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.logoUrl").value("/img/logo/삼성로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.teamId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.teamName").value("한화 이글스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.logoUrl").value("/img/logo/한화로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayVoteRate").value(0))

                // body[2]
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameId").value(430))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.teamId").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.teamName").value("NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.logoUrl").value("/img/logo/NC로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.teamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.teamName").value("롯데 자이언츠"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.logoUrl").value("/img/logo/롯데로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayVoteRate").value(0))

                // body[3]
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameId").value(431))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.teamId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.teamName").value("KT 위즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.logoUrl").value("/img/logo/KT로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.teamId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.teamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.logoUrl").value("/img/logo/LG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayVoteRate").value(0))

                // body[4]
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameId").value(432))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.teamId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.teamName").value("키움 히어로즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.logoUrl").value("/img/logo/키움로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.teamId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.teamName").value("KIA 타이거즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.logoUrl").value("/img/logo/KIA로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayVoteRate").value(0));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void find_my_predictions_test() throws Exception {
        // given
        String date = "2025-07-15";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/s/api/predictions")
                        .param("date", date)
                        .header("Authorization", "Bearer " + accessToken1)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.teamId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.teamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeam.logoUrl").value("/img/logo/두산로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.teamName").value("SSG 랜더스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeam.logoUrl").value("/img/logo/SSG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeScore").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].userChoiceTeamId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].predictionStatus").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeVoteRate").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameStatus").value("SCHEDULED"))

                // Game 2
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameId").value(424))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.teamId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.teamName").value("삼성 라이온즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeTeam.logoUrl").value("/img/logo/삼성로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.teamId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.teamName").value("한화 이글스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayTeam.logoUrl").value("/img/logo/한화로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeScore").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].userChoiceTeamId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].predictionStatus").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].homeVoteRate").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].awayVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameStatus").value("SCHEDULED"))

                // Game 3
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameId").value(425))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.teamId").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.teamName").value("NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeTeam.logoUrl").value("/img/logo/NC로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.teamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.teamName").value("롯데 자이언츠"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayTeam.logoUrl").value("/img/logo/롯데로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeScore").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].userChoiceTeamId").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].predictionStatus").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].homeVoteRate").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].awayVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameStatus").value("SCHEDULED"))

                // Game 4
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameId").value(426))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.teamId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.teamName").value("KT 위즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeTeam.logoUrl").value("/img/logo/KT로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.teamId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.teamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayTeam.logoUrl").value("/img/logo/LG로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].userChoiceTeamId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].predictionStatus").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].homeVoteRate").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].awayVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameStatus").value("SCHEDULED"))

                // Game 5
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameId").value(427))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameTime").value("18:30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.teamId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.teamName").value("키움 히어로즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeTeam.logoUrl").value("/img/logo/키움로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.teamId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.teamName").value("KIA 타이거즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayTeam.logoUrl").value("/img/logo/KIA로고.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeScore").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayScore").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].userChoiceTeamId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].predictionStatus").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].homeVoteRate").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].awayVoteRate").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameStatus").value("SCHEDULED"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    void save_test() throws Exception {
        // given
        List<Integer> gameIds = List.of(433, 434, 435, 436, 437);
        List<Integer> teamIds = List.of(2, 6, 9, 10, 3);

        List<UserPredictionRequest.SaveDTO> reqDTOs = new ArrayList<>();
        for (int i = 0; i < gameIds.size(); i++) {
            UserPredictionRequest.SaveDTO dto = new UserPredictionRequest.SaveDTO();
            dto.setGameId(gameIds.get(i));
            dto.setUserChoiceTeamId(teamIds.get(i));
            reqDTOs.add(dto);
        }


        String requestBody = om.writeValueAsString(reqDTOs);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/predictions")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken2)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameId").value(433));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].userChoiceTeamId").value(2));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gameId").value(434));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[1].userChoiceTeamId").value(6));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gameId").value(435));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[2].userChoiceTeamId").value(9));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gameId").value(436));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[3].userChoiceTeamId").value(10));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gameId").value(437));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[4].userChoiceTeamId").value(3));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
