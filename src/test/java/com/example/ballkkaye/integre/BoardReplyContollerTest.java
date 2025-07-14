package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.board.reply.BoardReplyRequest;
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

import static org.hamcrest.Matchers.nullValue;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardReplyContollerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        User ssar = User.builder().id(1).username("ssar").userRole(UserRole.USER).build();
        accessToken = JwtUtil.create(ssar);
    }

    // 댓글 작성
    @Test
    public void save_test() throws Exception {
        // given
        Integer baordId = 1;

        BoardReplyRequest.SaveDTO reqDTO = new BoardReplyRequest.SaveDTO();
        reqDTO.setTagReplyId(null);
        reqDTO.setParentReplyId(null);
        reqDTO.setContent("댓글 내용입니다.");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/boards/{id}/replies", baordId)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.replyId").value(29))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profileImg").value("/img/profile.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.parentReplyId").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.tagReplyId").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.tagReplyName").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("댓글 내용입니다."));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 대댓글 작성
    @Test
    public void save_child_reply_test() throws Exception {
        // given
        Integer baordId = 1;

        BoardReplyRequest.SaveDTO reqDTO = new BoardReplyRequest.SaveDTO();
        reqDTO.setTagReplyId(4);
        reqDTO.setParentReplyId(1);
        reqDTO.setContent("대댓글 내용입니다.");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/boards/{id}/replies", baordId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.replyId").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profileImg").value("/img/profile.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.parentReplyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.tagReplyId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.tagReplyName").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("대댓글 내용입니다."));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 댓글 삭제
    @Test
    public void delete_test() throws Exception {
        // given
        Integer replyId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/boards/replies/{id}", replyId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("삭제됨"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 댓글 수정
    @Test
    public void update_test() throws Exception {
        // given
        Integer replyId = 1;

        BoardReplyRequest.UpdateDTO reqDTO = new BoardReplyRequest.UpdateDTO();
        reqDTO.setTagReplyId(null);
        reqDTO.setContent("수정된 댓글 내용입니다.");

        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/boards/replies/{id}", replyId)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.replyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("수정된 댓글 내용입니다."));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 특정 게시글 댓글 조회
    @Test
    public void get_detail_test() throws Exception {
        // given
        Integer boardId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/boards/{id}/replies", boardId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                // 첫 번째 댓글
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].replyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].nickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].myTeamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].content").value("직관 최고였어요! 9회말 역전 감동이네요."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].likeCount").value(2))
                // 첫 번째 댓글의 첫 번째 대댓글
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].childReplies[0].replyId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].childReplies[0].nickname").value("haha"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].childReplies[0].myTeamName").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].childReplies[0].content").value("저도 그 장면에서 소리 질렀어요ㅋㅋ"))
                // 두 번째 대댓글
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].childReplies[1].tagReplyName").value("haha"))
                // 두 번째 부모 댓글
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].replyId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].nickname").value("cos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].myTeamName").value("두산 베어스"))
                // 세 번째 부모 댓글
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].replyId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].nickname").value("love"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].myTeamName").value("키움 히어로즈"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
