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
public class WeatherUltraControllerTest extends MyRestDoc {
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
    void get_today_forecast() throws Exception {
        // given
        Integer stadiumId = 2; // 잠실야구장 등 실제 stadiumId 존재해야 함

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/api/today/weather/{stadiumId}", stadiumId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 위치 정보 및 메시지 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.location").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.message").exists());

        // 온도/날씨 예보 sample 항목 하나 검사
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].hour").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].temperature").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].temperatureDiffFromYesterday").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].weatherCode").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].humidity").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].windDirection").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].windSpeed").exists());

        // 강수량 예보 sample 항목 하나 검사
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourlyRain[0].hour").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourlyRain[0].rainPer").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourlyRain[0].rainAmount").exists());

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
