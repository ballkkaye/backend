package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.board.BoardRequest;
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

import java.util.List;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

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
        reqDTO.setImages(List.of("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"));
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
        System.out.println(responseBody);

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

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[0].id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[0].imageUrl")
                .value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 커뮤니티 게시글 수정
    @Test
    public void update_test() throws Exception {
        // given
        Integer id = 1;
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO(
                "수정된 제목",
                1,
                "수정된 내용",
                List.of("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"),  // 존재하는 이미지 (중복 insert 방지됨)
                List.of("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image2.jpg")   // 새로 삽입될 이미지
        );

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

//        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/boards/{id}", id)
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("수정된 제목"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("수정된 내용"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[0].imageUrl")
                .value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imagesUrl[1].imageUrl")
                .value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image2.jpg"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    // 커뮤니티 게시글 목록 (page & teamId로 필터)
    @Test
    public void get_boards_page_and_team_id_test() throws Exception {
        // given
        Integer page = 1;
        Integer teamId = 3;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/boards")
                        .param("page", page.toString())
                        .param("teamId", teamId.toString())
                        .header("Authorization", "Bearer " + accessToken)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 팀 목록 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams.length()").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[0].teamName").value("LG 트윈스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[1].teamName").value("두산 베어스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[2].teamName").value("키움 히어로즈"));

        // 게시글 목록 검증 (index 기반)
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items.length()").value(5));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].boardId").value(6));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].replyCount").value(0));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].boardId").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].replyCount").value(0));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].boardId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].replyCount").value(0));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].boardId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].replyCount").value(0));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].boardId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].likeCount").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].replyCount").value(7));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);
    }

    // 커뮤니티 게시글 목록
    @Test
    public void get_baords_test() throws Exception {
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/boards")
                        .header("Authorization", "Bearer " + accessToken)
        );


        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 팀 목록 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams.length()").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[0].teamName").value("LG 트윈스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[1].teamName").value("두산 베어스"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.teams[2].teamName").value("키움 히어로즈"));

        // 게시글 목록 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items.length()").value(5));

        // boardId = 11
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].boardId").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].title").value("직관 다녀온 후기"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].teamId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].teamName").value("키움 히어로즈"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].replyCount").value(4));

        // boardId = 10
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].boardId").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].replyCount").value(3));

        // boardId = 9
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].boardId").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[2].replyCount").value(6));

        // boardId = 8
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].boardId").value(8));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[3].replyCount").value(0));

        // boardId = 7
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].boardId").value(7));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].likeCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.items[4].replyCount").value(0));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);
    }

    // 게시글 상세보기 조회 (게시글 + 댓글)
    @Test
    public void get_baord_test() throws Exception {
        // given
        Integer boardId = 1;

        String requestBody = om.writeValueAsString("boardId :" + boardId);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/boards/{id}", boardId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // 게시글 정보
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("직관 다녀온 후기"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(2));

        // 이미지 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.images.length()").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.images[0].imageUrl").value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.images[1].imageUrl").value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image2.jpg"));

        // 댓글(부모) 개수
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems.length()").value(3));

        // 첫 번째 부모 댓글
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[0].replyId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[0].content").value("직관 최고였어요! 9회말 역전 감동이네요."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[0].childReplies.length()").value(3));

        // 첫 번째 대댓글
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[0].childReplies[0].replyId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[0].childReplies[0].content").value("저도 그 장면에서 소리 질렀어요ㅋㅋ"));

        // 두 번째 부모 댓글
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[1].replyId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[1].childReplies.length()").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[1].childReplies[0].replyId").value(7));

        // 세 번째 부모 댓글
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[2].replyId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replyItems[2].childReplies.length()").value(0));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);
    }


    // 게시글 삭제
    @Test
    public void delete_test() throws Exception {
        // given
        Integer boardId = 1;
        String requestBody = om.writeValueAsString("boardId :" + boardId);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/boards/{id}", boardId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("삭제됨"));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);
    }

    // 게시글 상세보기 (게시글)
    @Test
    public void detail_test() throws Exception {
        // given
        Integer boardId = 1;
        String requestBody = om.writeValueAsString("boardId :" + boardId);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/boards/{id}/detail", boardId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.boardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.nickname").value("ssar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profileImageUrl").value("/img/profile.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.relativeTime").value("2주 전"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.myTeamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamCategoryId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.teamCategoryName").value("키움 히어로즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("직관 다녀온 후기"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("롯데 경기 진짜 재밌었음. 9회말 역전승은 감동이었어."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.isLike").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.likeCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.images.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.images[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.images[0].imageUrl").value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image1.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.images[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.images[1].imageUrl").value("https://ballkkaye-bucket.s3.ap-northeast-2.amazonaws.com/test/image2.jpg"));
    }
}
