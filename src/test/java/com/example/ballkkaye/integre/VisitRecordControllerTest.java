package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.stadium.StadiumRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import com.example.ballkkaye.visitRecord.VisitRecord;
import com.example.ballkkaye.visitRecord.VisitRecordRepository;
import com.example.ballkkaye.visitRecord.VisitRecordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class VisitRecordControllerTest extends MyRestDoc {


    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private VisitRecordRepository visitRecordRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 외래키 무시 + 전 테이블 truncate
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        em.createNativeQuery("TRUNCATE TABLE visit_record_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE user_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE team_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_correction_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_coordinate_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE today_game_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE game_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE board_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE weather_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE weather_ultra_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE player_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE today_starting_pitcher_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE today_hitter_lineup_tb RESTART IDENTITY").executeUpdate();

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();


        Stadium stadium1 = stadiumRepository.save(Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
                .stadiumType(StadiumType.OUTDOOR)
                .build());

        Stadium stadium2 = stadiumRepository.save(Stadium.builder()
                .stadiumName("고척스카이돔")
                .location("서울 구로구")
                .stadiumType(StadiumType.INDOOR)
                .build());


        Stadium stadium5 = stadiumRepository.save(Stadium.builder()
                .stadiumName("대구 삼성라이온즈파크")
                .location("대구 수성구")
                .stadiumType(StadiumType.OUTDOOR)
                .build());

        Team homeTeam1 = teamRepository.save(Team.builder()
                .stadium1(stadium1)
                .teamName("LG 트윈스")
                .logoUrl("/img/team/lg.png")
                .ticketLink("https://www.lgtwins.com")
                .build());

        Team homeTeam2 = teamRepository.save(Team.builder()
                .stadium1(stadium1)
                .teamName("두산 베어스")
                .logoUrl("/img/team/doosan.png")
                .ticketLink("https://www.doosanbears.com")
                .build());

        Team awayTeam1 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("삼성 라이온즈")
                .logoUrl("/img/team/samsung.png")
                .ticketLink("https://www.samsunglions.com")
                .build());

        Team awayTeam2 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("롯데 자이언츠")
                .logoUrl("/img/team/lotte.png")
                .ticketLink("https://www.giantsclub.com")
                .build());



        User user = User.builder()
                .username("ssarssar123")
                .password("1234")
                .name("쌀")
                .nickname("ssar")
                .team(awayTeam2)
                .phoneNumber("01011112222")
                .email("ssar@nate.com")
                .birthDate(LocalDate.of(1999, 9, 9))
                .gender(Gender.MALE)
                .profileUrl("/img/profile.png")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .predictionScore(0)
                .predictionTier(PredictionTier.IRON)
                .fcmToken("1111")
                .build();

        userRepository.save(user);
        accessToken = JwtUtil.create(user);


        // LG vs 삼성
        Game game1 = gameRepository.save(Game.builder()
                .stadium(stadium1)
                .homeTeam(homeTeam1)
                .awayTeam(awayTeam1)
                .gameTime(Timestamp.valueOf(LocalDateTime.of(2025, 7, 5, 14, 0)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SS_T)
                .awayResultScore(2)
                .homeResultScore(3)
                .homePredictionScore(45.0)
                .awayPredictionScore(55.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.45)
                .awayWinPer(0.55)
                .build());

        // 두산 vs 롯데
        Game game2 = gameRepository.save(Game.builder()
                .stadium(stadium2)
                .homeTeam(homeTeam2)
                .awayTeam(awayTeam2)
                .gameTime(Timestamp.valueOf(LocalDateTime.of(2025, 7, 9, 18, 30)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SPO_T)
                .awayResultScore(1)
                .homeResultScore(5)
                .homePredictionScore(50.0)
                .awayPredictionScore(50.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.50)
                .awayWinPer(0.50)
                .build());


        VisitRecord visitRecord1 = VisitRecord.builder()
                .game(game1)
                .team(awayTeam2)
                .user(user)
                .content("1번째 직관 기록입니다.")
                .result(Result.WIN)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .imgUrl("https://example.com/record1.jpg")
                .build();

        VisitRecord visitRecord2 = VisitRecord.builder()
                .game(game2)
                .team(homeTeam2)
                .user(user)
                .content("2번째 직관 기록입니다.")
                .result(Result.LOSE)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .imgUrl("https://example.com/record2.jpg")
                .build();

        visitRecordRepository.save(visitRecord1);
        visitRecordRepository.save(visitRecord2);

    }


    @Test
    public void save_test() throws Exception {
        // given
        Integer gameId = 1;
        Integer teamId = 2;
        Result result = Result.fromValue("승");
        String content = "응원 열심히 했고, 분위기 최고였어요!";
        String imgUrl = "https://example.com/visit-photo.jpg";

        VisitRecordRequest.SaveDTO reqDTO = new VisitRecordRequest.SaveDTO(gameId, teamId, result, content, imgUrl);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/visit-records")
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("LG"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("삼성"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025.07.05"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("승"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("응원 열심히 했고, 분위기 최고였어요!"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://example.com/visit-photo.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("정상"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


    @Test
    public void update_test() throws Exception {
        // given
        Integer visitRecordId = 1;
        Result result = Result.fromValue("패");
        String content = "삼성 노잼 ~";
        String imgUrl = "https://example.com/visit-photo.jpg";

        VisitRecordRequest.UpdateDTO reqDTO = new VisitRecordRequest.UpdateDTO();
        reqDTO.setResult(result);
        reqDTO.setContent(content);
        reqDTO.setImgUrl(imgUrl);

        String requestBody = om.writeValueAsString(reqDTO);
        //System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/visit-records/" + visitRecordId)
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("LG"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("삼성"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value("2025.07.05"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("패"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("삼성 노잼 ~"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://example.com/visit-photo.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.deleteStatus").value("정상"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    // 특정 날짜
    @Test
    public void get_list_date_test() throws Exception {
        // given
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));


        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records")
                        .param("date", LocalDate.now().toString())
                        .accept(MediaType.APPLICATION_JSON)  // 받을 데이터의 타입 명시
                        .header("Authorization", "Bearer " + accessToken)

        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamName").value("두산"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamName").value("롯데"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeScore").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayScore").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameDate").value("2025.07.09"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("고척스카이돔"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    // 특정 월
    @Test
    public void get_list_year_and_month_test() throws Exception {
        // given
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String month = String.valueOf(LocalDate.now().getMonthValue());


        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records")
                        .param("year", year)
                        .param("month", month)
                        .accept(MediaType.APPLICATION_JSON)  // 받을 데이터의 타입 명시
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeTeamName").value("두산"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayTeamName").value("롯데"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].homeScore").value(5));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].awayScore").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].gameDate").value("2025.07.09"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body[0].stadiumName").value("고척스카이돔"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    @Test
    public void get_detail_test() throws Exception {
        // given
        String gameDate = "2025.07.05";
        Integer visitRecordId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/visit-records/" + visitRecordId)
                        .accept(MediaType.APPLICATION_JSON)  // 받을 데이터의 타입 명시
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeTeamName").value("LG"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayTeamName").value("삼성"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.homeScore").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.awayScore").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameDate").value(gameDate));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.stadiumName").value("잠실야구장"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("승"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("1번째 직관 기록입니다."));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.imgUrl").value("https://example.com/record1.jpg"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    @Test
    public void delete_test() throws Exception {
        // given
        Integer visitRecordId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/visit-records/" + visitRecordId)
                        .header("Authorization", "Bearer " + accessToken)
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
