package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.match.MatchRequest;
import com.example.ballkkaye.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
public class MatchControllerTest extends MyRestDoc {

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
        MatchRequest.SaveDTO reqDTO = new MatchRequest.SaveDTO(
                443,                       // gameId
                7,                         // teamId
                5,                         // maxParticipants
                Gender.FEMALE,             // preferredGender
                Age.FROM_20_TO_30,              // preferredAge
                "같이 직관 가실 분!",         // title
                "3루에서 보실 분 구해요~",      // content
                false                       // isSameTeam
        );

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/matches")
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

                // chatRoom 정보 검증
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.gameId").value(443))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.matchTitle").value("롯데 자이언츠 vs NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.preferredTeamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.preferredGender").value("여성"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.preferredAge").value("20~30대"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoom.isSameTeam").value(false))

                // match 정보 검증
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.match.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.match.title").value("같이 직관 가실 분!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.match.content").value("3루에서 보실 분 구해요~"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.match.userNickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.match.userTeamName").value("LG 트윈스"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void get_matches_test() throws Exception {
        // given
        Integer page = 0;
        Gender gender = Gender.FEMALE;
        Age age = Age.FROM_20_TO_30;
        Integer teamId = 7;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/matches")
                        .param("page", page.toString())
                        .param("gender", gender.name())
                        .param("age", age.name())
                        .param("teamId", teamId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedGender").value("여성"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedAge").value("20~30대"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedTeamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.selectedTimeName").value("롯데 자이언츠"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].participationInfo").value("2/5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].relativeTime")
                        .value(Matchers.matchesPattern(".*시간 전.*")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].homeTeamName").value("NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].awayTeamName").value("롯데 자이언츠"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].title").value("기아 경기 같이 응원할 친구 구해요"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].gender").value("여성"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].age").value("20~30대"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].isSameTeam").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].matchId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].teamId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matches[0].teamName").value("롯데 자이언츠"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    void get_detail_test() throws Exception {
        // given
        int matchId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/matches/{id}", matchId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.relativeTime")
                        .value(Matchers.matchesPattern(".*시간 전.*")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.matchId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("SSG 랜더스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025-07-16"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userNickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userTeamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userProfileUrl").value("/img/profile.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("혹시 내일 같이 야구 볼 사람?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("야구 좋아하는 사람 모여라! 롯데 팬이면 더 좋고, 같이 맛있는 거 먹으면서 직관할 사람 구합니다. 편하게 연락 주세요!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gender").value("무관"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.age").value("연령 무관"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isSameTeam").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.participationInfo").value("1/5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoomId").value(1));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    void update_test() throws Exception {
        // given
        int matchId = 1;

        MatchRequest.UpdateDTO reqDTO = new MatchRequest.UpdateDTO(
                "제목 수정함",
                "내용도 수정함",
                443,                       // gameId
                7,                         // teamId
                false,                     // isSameTeam
                5,                         // maxParticipants
                Gender.FEMALE,            // preferredGender
                Age.FROM_20_TO_30         // preferredAge
        );

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/matches/{id}", matchId)
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

                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.relativeTime")
                        .value(Matchers.matchesPattern("\\d+시간 전")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameTitle").value("롯데 자이언츠 vs NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025-08-07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("부산 사직야구장"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userNickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userTeamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userProfileUrl").value("/img/profile.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목 수정함"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용도 수정함"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.gender").value("여성"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.age").value("20~30대"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isSameTeam").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.participationInfo").value("1/5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.chatRoomId").value(1));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    void delete_test() throws Exception {
        // given
        int matchId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/matches/" + matchId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("DELETED"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
