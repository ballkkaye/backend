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
public class TodayTeamRecordControllerTest extends MyRestDoc {
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
    void team_records_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/s/api/today/team-records")
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body").isArray())

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].teamName").value("한화 이글스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gap").value(0.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].winGame").value(46))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].loseGame").value(33))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].tieGame").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].totalGame").value(80))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].winRate").value(0.582))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].teamRank").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].recentTenGame").value("4승0무6패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].streak").value("1패"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].teamName").value("LG 트윈스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].gap").value(1.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].winGame").value(45))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].loseGame").value(34))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].tieGame").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].totalGame").value(81))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].winRate").value(0.57))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].teamRank").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].recentTenGame").value("4승0무6패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].streak").value("1패"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].teamName").value("롯데 자이언츠"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].gap").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].winGame").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].loseGame").value(35))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].totalGame").value(82))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].winRate").value(0.557))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].teamRank").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].recentTenGame").value("6승0무4패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].streak").value("1승"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].teamName").value("KIA 타이거즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].gap").value(3.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].winGame").value(42))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].loseGame").value(36))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].totalGame").value(81))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].winRate").value(0.538))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].teamRank").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].recentTenGame").value("5승2무3패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[3].streak").value("1패"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].teamName").value("SSG 랜더스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].gap").value(5.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].winGame").value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].loseGame").value(37))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].totalGame").value(80))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].winRate").value(0.519))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].teamRank").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].recentTenGame").value("5승1무4패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[4].streak").value("1승"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].teamName").value("KT 위즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].gap").value(5.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].winGame").value(41))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].loseGame").value(38))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].totalGame").value(82))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].winRate").value(0.519))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].teamRank").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].recentTenGame").value("5승0무5패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[5].streak").value("1승"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].teamName").value("삼성 라이온즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].gap").value(6.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].winGame").value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].loseGame").value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].tieGame").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].totalGame").value(81))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].winRate").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].teamRank").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].recentTenGame").value("2승0무8패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[6].streak").value("1패"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].teamName").value("NC 다이노스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].gap").value(7.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].winGame").value(36))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].loseGame").value(38))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].tieGame").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].totalGame").value(78))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].winRate").value(0.486))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].teamRank").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].recentTenGame").value("6승0무4패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[7].streak").value("1승"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].teamName").value("두산 베어스"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].gap").value(13.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].winGame").value(32))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].loseGame").value(46))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].totalGame").value(81))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].winRate").value(0.41))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].teamRank").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].recentTenGame").value("5승0무5패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[8].streak").value("1승"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].teamName").value("키움 히어로즈"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].gap").value(21.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].winGame").value(26))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].loseGame").value(55))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].tieGame").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].totalGame").value(84))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].winRate").value(0.321))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].teamRank").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].recentTenGame").value("5승1무4패"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[9].streak").value("1패"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
