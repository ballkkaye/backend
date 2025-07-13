package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class TeamControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    // 팀 목록 조회 (옵션박스용)
    @Test
    public void get_teams_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.length()").value(10))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].teamId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].teamName").value("LG 트윈스"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].teamId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].teamName").value("두산 베어스"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].teamId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].teamName").value("키움 히어로즈"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].teamId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].teamName").value("SSG 랜더스"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].teamId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].teamName").value("KIA 타이거즈"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].teamId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].teamName").value("삼성 라이온즈"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].teamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].teamName").value("롯데 자이언츠"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].teamId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].teamName").value("한화 이글스"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].teamId").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].teamName").value("NC 다이노스"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].teamId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].teamName").value("KT 위즈"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

}
