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
public class GameControllerTest extends MyRestDoc {

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
    void get_games_test() throws Exception {
        // given
        String date = "2025-07";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/games")
                        .param("date", date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);
        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].gameDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].gameId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].homeTeamId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].awayTeamId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].homeTeamScore").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].awayTeamScore").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedDate").value("2025-07"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    // 년월일까지 조회
    @Test
    void get_games_by_day_test() throws Exception {
        // given
        String date = "2025-07-15";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/games")
                        .param("date", date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].gameDate").value("2025.07.15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].gameId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].homeTeamId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].awayTeamId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].homeTeamScore").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.games[0].items[0].awayTeamScore").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedDate").value("2025-07-15"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_calendar_game_test() throws Exception {
        // given
        String date = "2025-07";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/games/dates")
                        .param("date", date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].year").value("2025"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].month").value("07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[0].day").value("14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[0].isHaveGame").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[1].day").value("15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[1].isHaveGame").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[2].day").value("16"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[2].isHaveGame").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[3].day").value("17"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].monthDTO[0].day[3].isHaveGame").value(true));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
