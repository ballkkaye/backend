package com.example.ballkkaye.integre;


import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRequest;
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
public class UserControllerTest extends MyRestDoc {

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
    void get_additional_user_info_test() throws Exception {
        // given
        UserRequest.AdditionalInfoDTO reqDTO = new UserRequest.AdditionalInfoDTO();
        reqDTO.setNickname("테스터");
        reqDTO.setTeamId(3);

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/user/addtion-info")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken2)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("cos123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("코스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("테스터"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamName").value("키움 히어로즈"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.phoneNumber").value("01022223333"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("cos@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value("2000-01-01"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gender").value("FEMALE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.profileUrl").value("/img/profile.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.providerType").value("BALLKKAYE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userRole").value("USER"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void check_user_nickname_available_fail_test() throws Exception {
        // given
        String nickname = "ssar";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/users/check-nickname-available/{nickname}", nickname)
                        .header("Authorization", "Bearer " + accessToken2)
        );
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.available").value(false));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void check_user_nickname_available_test() throws Exception {
        // given
        String nickname = "test";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/users/check-nickname-available/{nickname}", nickname)
                        .header("Authorization", "Bearer " + accessToken2)
        );
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.available").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void update_test() throws Exception {
        // given
        UserRequest.UpdateDTO reqDTO = new UserRequest.UpdateDTO();
        reqDTO.setNickname("업데이트닉네임12");
        reqDTO.setTeamId(2);
        reqDTO.setProfileImg("https://your-bucket.s3.ap-northeast-2.amazonaws.com/profile123.png");


        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken1)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("ssar123"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("업데이트닉네임12"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamName").value("두산 베어스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.phoneNumber").value("01011112222"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.birthDate").value("1999-09-09"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gender").value("MALE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.profileUrl").value("https://your-bucket.s3.ap-northeast-2.amazonaws.com/profile123.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.providerType").value("BALLKKAYE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userRole").value("USER"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_score_and_tier_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/users/tier")
                        .header("Authorization", "Bearer " + accessToken2)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println("response = " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.score").value(50));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.tier").value("IRON"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userNickname").value("cos"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }


}
