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
public class ChatRoomUserControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String accessToken1;
    private String accessToken2;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").userRole(UserRole.USER).build();
        accessToken1 = JwtUtil.create(ssar);
        User cos = User.builder().id(2).username("cos").userRole(UserRole.USER).build();
        accessToken2 = JwtUtil.create(cos);
    }

    @Test
    void save_test() throws Exception {
        // given
        Integer chatRoomId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/s/api/chatrooms/{id}", chatRoomId)
                        .header("Authorization", "Bearer " + accessToken2)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoomUserId").value(9));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void delete_test() throws Exception {
        // given
        Integer chatRoomUserId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/chatroom-users/{id}", chatRoomUserId)
                        .header("Authorization", "Bearer " + accessToken1)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("삭제됨"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
