package com.example.ballkkaye.integre;


import com.example.ballkkaye.MyRestDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TodayHitterLineupControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;


//    @Test
//    public void get_matchup_details_test() throws Exception {
//        // when
//        ResultActions actions = mvc.perform(
//                MockMvcRequestBuilders
//                        .get("/api/today/hitter-lineup")
//                        .param("gameId", "423")
//                        .param("teamId", "2")
//                        .accept(MediaType.APPLICATION_JSON)  // 받을 데이터의 타입 명시
//
//        );
//
//        // eye
//        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        System.out.println(responseBody);
//
//
//        // then
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameId").value(423));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.season").value(2025));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("김건우"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameCount").value(27));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("2승 3패"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.qs").value(0));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.whip").value(1.53));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.era").value(4.58));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.profileUrl").value("https://6ptotvmi5753.edge.naverncp.com/KBO_IMAGE/person/kbo/2025/51867.png"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters", hasSize(9)));
//        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
//
//
//    }


}
