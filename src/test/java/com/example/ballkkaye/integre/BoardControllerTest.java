package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.board.BoardRequest;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.stadium.StadiumRepository;
import com.example.ballkkaye.team.TeamRepository;
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

import java.util.List;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamRepository teamRepository;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").userRole(UserRole.USER).build();
        accessToken = JwtUtil.create(ssar);
    }

    // 커뮤니티 게시글 등록
    @Test
    public void save_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("제목1");
        reqDTO.setTeamId(1);
        reqDTO.setImages(List.of("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg\n"));
        reqDTO.setContent("내용1");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/boards")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(12));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.relativeTime").value("방금"));

        // 이미지 리스트 존재 여부
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[0].id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[0].imageUrl").value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg\n"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
