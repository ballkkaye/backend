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
public class StadiumControllerTest extends MyRestDoc {

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
    void get_stadium_options_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/stadiums")
                        .contentType(MediaType.APPLICATION_JSON)
        );


        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);
        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.length()").value(13));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("잠실야구장"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[1].stadiumId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[1].stadiumName").value("고척스카이돔"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[2].stadiumId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[2].stadiumName").value("인천 SSG 랜더스필드"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[3].stadiumId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[3].stadiumName").value("광주-기아 챔피언스필드"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[4].stadiumId").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[4].stadiumName").value("대구 삼성라이온즈파크"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[5].stadiumId").value(6));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[5].stadiumName").value("부산 사직야구장"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[6].stadiumId").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[6].stadiumName").value("대전 한화생명이글스파크"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[7].stadiumId").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[7].stadiumName").value("창원 NC파크"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[8].stadiumId").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[8].stadiumName").value("수원 KT위즈파크"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[9].stadiumId").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[9].stadiumName").value("청주야구장"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[10].stadiumId").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[10].stadiumName").value("울산 문수야구장"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[11].stadiumId").value(12));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[11].stadiumName").value("포항야구장"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[12].stadiumId").value(13));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[12].stadiumName").value("군산 월명야구장"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}

