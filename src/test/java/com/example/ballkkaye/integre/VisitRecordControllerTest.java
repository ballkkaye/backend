package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.Result;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.visitRecord.VisitRecordRequest;
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
public class VisitRecordControllerTest extends MyRestDoc {

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
        VisitRecordRequest.SaveDTO reqDTO = new VisitRecordRequest.SaveDTO(
                423,
                3,
                Result.WIN,
                "응원 열심히 했고, 분위기 최고였어요!",
                "https://example.com/visit-photo.jpg"
        );

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/visit-records")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025.07.15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("승"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("응원 열심히 했고, 분위기 최고였어요!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://example.com/visit-photo.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("정상"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void update_test() throws Exception {
        // given
        Integer visitRecordId = 1;
        VisitRecordRequest.UpdateDTO reqDTO = new VisitRecordRequest.UpdateDTO(
                Result.WIN,
                "테스트용 수정 내용입니다~~",
                "https://update.com/visit-photo.jpg"
        );

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/visit-records/{id}", visitRecordId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025.07.14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("승"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("테스트용 수정 내용입니다~~"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://update.com/visit-photo.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("정상"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


    @Test
    void get_list_date_test() throws Exception {
        // given
        String date = "2025-07-16";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records")
                        .param("date", date)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeScore").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameDate").value("2025.07.14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("잠실야구장"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_list_year_and_month_test() throws Exception {
        // given
        String year = "2025";
        String month = "7";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records")
                        .param("year", year)
                        .param("month", month)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeScore").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameDate").value("2025.07.14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("잠실야구장"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_detail_test() throws Exception {
        // given
        Integer visitRecordId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records/{id}", visitRecordId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("두산"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("SSG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025.07.14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("승"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("응원 열심히 했고, 분위기 최고였어요!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://example.com/photo1.jpg"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void delete_test() throws Exception {
        // given
        Integer visitRecordId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/visit-records/{id}", visitRecordId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("삭제됨"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
