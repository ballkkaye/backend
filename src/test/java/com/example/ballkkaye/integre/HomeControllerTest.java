package com.example.ballkkaye.integre;


import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.player.Player;
import com.example.ballkkaye.player.PlayerRepository;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcherRepository;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.stadium.StadiumRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class HomeControllerTest extends MyRestDoc {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TodayGameRepository todayGameRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TodayStartingPitcherRepository todayStartingPitcherRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        // 외래키 무시 + 전 테이블 truncate
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        em.createNativeQuery("TRUNCATE TABLE team_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_correction_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_coordinate_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE stadium_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE visit_record_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE today_game_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE game_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE board_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE weather_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE weather_ultra_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE player_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE today_starting_pitcher_tb RESTART IDENTITY").executeUpdate();

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();


        // given
        User user = User.builder()
                .username("hoho123")
                .email("hoho123@gmail.com")
                .providerType(ProviderType.BALLKKAYE)
                .userRole(UserRole.USER)
                .name("호호")
                .password("123456")
                .nickname("호호호")
                .phoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(1999, 12, 31))
                .profileUrl("https://www.google.com")
                .predictionScore(0)
                .predictionTier(PredictionTier.NONE)
                .gender(Gender.MALE)
                .build();

        userRepository.save(user);

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

        Stadium stadium3 = stadiumRepository.save(Stadium.builder()
                .stadiumName("인천 SSG 랜더스필드")
                .location("인천 미추홀구")
                .stadiumType(StadiumType.OUTDOOR)
                .build());

        Stadium stadium4 = stadiumRepository.save(Stadium.builder()
                .stadiumName("광주-기아 챔피언스필드")
                .location("광주 북구")
                .stadiumType(StadiumType.OUTDOOR)
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

        Team homeTeam3 = teamRepository.save(Team.builder()
                .stadium1(stadium2)
                .teamName("키움 히어로즈")
                .logoUrl("/img/team/kiwoom.png")
                .ticketLink("https://www.heroesbaseball.co.kr")
                .build());

        Team homeTeam4 = teamRepository.save(Team.builder()
                .stadium1(stadium3)
                .teamName("SSG 랜더스")
                .logoUrl("/img/team/ssg.png")
                .ticketLink("https://www.ssglanders.com")
                .build());

        Team homeTeam5 = teamRepository.save(Team.builder()
                .stadium1(stadium4)
                .teamName("KIA 타이거즈")
                .logoUrl("/img/team/kia.png")
                .ticketLink("https://www.tigers.co.kr")
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

        Team awayTeam3 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("한화 이글스")
                .logoUrl("/img/team/hanwha.png")
                .ticketLink("https://www.hanwhaeagles.co.kr")
                .build());

        Team awayTeam4 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("NC 다이노스")
                .logoUrl("/img/team/nc.png")
                .ticketLink("https://www.ncdinos.com")
                .build());

        Team awayTeam5 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("KT 위즈")
                .logoUrl("/img/team/kt.png")
                .ticketLink("https://www.ktwiz.co.kr")
                .build());


        Player player1 = Player.builder()
                .kboPlayerId(51867)
                .name("김건우")
                .team(homeTeam1) // LG 트윈스
                .build();

        Player player2 = Player.builder()
                .kboPlayerId(55257)
                .name("콜어빈")
                .team(homeTeam2) // 두산 베어스
                .build();

        Player player3 = Player.builder()
                .kboPlayerId(52701)
                .name("문동주")
                .team(homeTeam3) // 키움 히어로즈
                .build();

        Player player4 = Player.builder()
                .kboPlayerId(55460)
                .name("가라비토")
                .team(homeTeam4) // SSG 랜더스
                .build();

        Player player5 = Player.builder()
                .kboPlayerId(55532)
                .name("감보아")
                .team(homeTeam5) // KIA 타이거즈
                .build();

        Player player6 = Player.builder()
                .kboPlayerId(66920)
                .name("최성영")
                .team(awayTeam1) // 삼성 라이온즈
                .build();

        Player player7 = Player.builder()
                .kboPlayerId(52341)
                .name("나균안")
                .team(awayTeam2) // 롯데 자이언츠
                .build();

        Player player8 = Player.builder()
                .kboPlayerId(54828)
                .name("펠릭스")
                .team(awayTeam3) // 한화 이글스
                .build();

        Player player9 = Player.builder()
                .kboPlayerId(56780)
                .name("이재학")
                .team(awayTeam4) // NC 다이노스
                .build();

        Player player10 = Player.builder()
                .kboPlayerId(51337)
                .name("고영표")
                .team(awayTeam5) // KT 위즈
                .build();

        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.save(player4);
        playerRepository.save(player5);
        playerRepository.save(player6);
        playerRepository.save(player7);
        playerRepository.save(player8);
        playerRepository.save(player9);
        playerRepository.save(player10);


        Game game1 = gameRepository.save(Game.builder()
                .stadium(stadium1)
                .homeTeam(homeTeam1)
                .awayTeam(awayTeam1)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SS_T)
                .homePredictionScore(45.0)
                .awayPredictionScore(55.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.45)
                .awayWinPer(0.55)
                .build());

        Game game2 = gameRepository.save(Game.builder()
                .stadium(stadium2)
                .homeTeam(homeTeam2)
                .awayTeam(awayTeam2)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(2)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SPO_T)
                .homePredictionScore(50.0)
                .awayPredictionScore(50.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.50)
                .awayWinPer(0.50)
                .build());

        Game game3 = gameRepository.save(Game.builder()
                .stadium(stadium3)
                .homeTeam(homeTeam3)
                .awayTeam(awayTeam3)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.K_2T)
                .homePredictionScore(52.0)
                .awayPredictionScore(48.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.52)
                .awayWinPer(0.48)
                .build());

        Game game4 = gameRepository.save(Game.builder()
                .stadium(stadium4)
                .homeTeam(homeTeam4)
                .awayTeam(awayTeam4)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(4)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.M_T)
                .homePredictionScore(43.0)
                .awayPredictionScore(57.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.43)
                .awayWinPer(0.57)
                .build());

        Game game5 = gameRepository.save(Game.builder()
                .stadium(stadium5)
                .homeTeam(homeTeam5)
                .awayTeam(awayTeam5)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(5)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SPO_2T)
                .homePredictionScore(49.0)
                .awayPredictionScore(51.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.49)
                .awayWinPer(0.51)
                .build());


        todayGameRepository.save(TodayGame.builder()
                .stadium(stadium1)
                .homeTeam(homeTeam1)
                .awayTeam(awayTeam1)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SS_T)
                .homePredictionScore(45.0)
                .awayPredictionScore(55.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.45)
                .awayWinPer(0.55)
                .game(game1)
                .build());


        todayGameRepository.save(TodayGame.builder()
                .stadium(stadium2)
                .homeTeam(homeTeam2)
                .awayTeam(awayTeam2)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(2)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SPO_T)
                .homePredictionScore(50.0)
                .awayPredictionScore(50.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.50)
                .awayWinPer(0.50)
                .game(game2)
                .build());

        todayGameRepository.save(TodayGame.builder()
                .stadium(stadium3)
                .homeTeam(homeTeam3)
                .awayTeam(awayTeam3)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.K_2T)
                .homePredictionScore(52.0)
                .awayPredictionScore(48.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.52)
                .awayWinPer(0.48)
                .game(game3)
                .build());

        todayGameRepository.save(TodayGame.builder()
                .stadium(stadium4)
                .homeTeam(homeTeam4)
                .awayTeam(awayTeam4)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(4)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.M_T)
                .homePredictionScore(43.0)
                .awayPredictionScore(57.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.43)
                .awayWinPer(0.57)
                .game(game4)
                .build());

        todayGameRepository.save(TodayGame.builder()
                .stadium(stadium5)
                .homeTeam(homeTeam5)
                .awayTeam(awayTeam5)
                .gameTime(Timestamp.valueOf(LocalDateTime.now().plusHours(5)))
                .gameStatus(GameStatus.SCHEDULED)
                .broadcastChannel(BroadcastChannel.SPO_2T)
                .homePredictionScore(49.0)
                .awayPredictionScore(51.0)
                .totalPredictionScore(100.0)
                .homeWinPer(0.49)
                .awayWinPer(0.51)
                .game(game5)
                .build());

        // game1 - LG vs 삼성
        TodayStartingPitcher pitcher1 = TodayStartingPitcher.builder()
                .game(game1)
                .player(player1) // LG 트윈스 (home)
                .profileUrl("https://example.com/player1.png")
                .ERA(3.12)
                .gameCount(14)
                .result("6승 4패")
                .QS(9)
                .WHIP(1.15)
                .build();

        TodayStartingPitcher pitcher2 = TodayStartingPitcher.builder()
                .game(game1)
                .player(player6) // 삼성 라이온즈 (away)
                .profileUrl("https://example.com/player6.png")
                .ERA(2.88)
                .gameCount(15)
                .result("7승 5패")
                .QS(10)
                .WHIP(1.10)
                .build();

// game2 - 두산 vs 롯데
        TodayStartingPitcher pitcher3 = TodayStartingPitcher.builder()
                .game(game2)
                .player(player2) // 두산 (home)
                .profileUrl("https://example.com/player2.png")
                .ERA(3.45)
                .gameCount(13)
                .result("5승 4패")
                .QS(8)
                .WHIP(1.22)
                .build();

        TodayStartingPitcher pitcher4 = TodayStartingPitcher.builder()
                .game(game2)
                .player(player7) // 롯데 (away)
                .profileUrl("https://example.com/player7.png")
                .ERA(4.01)
                .gameCount(12)
                .result("4승 6패")
                .QS(7)
                .WHIP(1.30)
                .build();

// game3 - 키움 vs 한화
        TodayStartingPitcher pitcher5 = TodayStartingPitcher.builder()
                .game(game3)
                .player(player3) // 키움 (home)
                .profileUrl("https://example.com/player3.png")
                .ERA(3.76)
                .gameCount(16)
                .result("6승 5패")
                .QS(10)
                .WHIP(1.18)
                .build();

        TodayStartingPitcher pitcher6 = TodayStartingPitcher.builder()
                .game(game3)
                .player(player8) // 한화 (away)
                .profileUrl("https://example.com/player8.png")
                .ERA(3.65)
                .gameCount(14)
                .result("5승 5패")
                .QS(9)
                .WHIP(1.25)
                .build();

// game4 - SSG vs NC
        TodayStartingPitcher pitcher7 = TodayStartingPitcher.builder()
                .game(game4)
                .player(player4) // SSG (home)
                .profileUrl("https://example.com/player4.png")
                .ERA(3.98)
                .gameCount(15)
                .result("6승 6패")
                .QS(7)
                .WHIP(1.33)
                .build();

        TodayStartingPitcher pitcher8 = TodayStartingPitcher.builder()
                .game(game4)
                .player(player9) // NC (away)
                .profileUrl("https://example.com/player9.png")
                .ERA(3.20)
                .gameCount(16)
                .result("8승 4패")
                .QS(11)
                .WHIP(1.17)
                .build();

// game5 - KIA vs KT
        TodayStartingPitcher pitcher9 = TodayStartingPitcher.builder()
                .game(game5)
                .player(player5) // KIA (home)
                .profileUrl("https://example.com/player5.png")
                .ERA(2.91)
                .gameCount(13)
                .result("7승 3패")
                .QS(9)
                .WHIP(1.08)
                .build();

        TodayStartingPitcher pitcher10 = TodayStartingPitcher.builder()
                .game(game5)
                .player(player10) // KT (away)
                .profileUrl("https://example.com/player10.png")
                .ERA(3.10)
                .gameCount(14)
                .result("6승 5패")
                .QS(10)
                .WHIP(1.12)
                .build();

        todayStartingPitcherRepository.save(pitcher1);
        todayStartingPitcherRepository.save(pitcher2);
        todayStartingPitcherRepository.save(pitcher3);
        todayStartingPitcherRepository.save(pitcher4);
        todayStartingPitcherRepository.save(pitcher5);
        todayStartingPitcherRepository.save(pitcher6);
        todayStartingPitcherRepository.save(pitcher7);
        todayStartingPitcherRepository.save(pitcher8);
        todayStartingPitcherRepository.save(pitcher9);
        todayStartingPitcherRepository.save(pitcher10);


        Board board1 = Board.builder()
                .user(user)
                .team(null)
                .title("첫 게시글입니다!")
                .content("오늘 경기 정말 재미있었어요. 다음에도 꼭 가고 싶네요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        Board board2 = Board.builder()
                .user(user)
                .team(null)
                .title("두번째 게시글입니다!")
                .content("오늘 경기 정말 재미있었어요. 다음에도 꼭 가고 싶네요!")
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);

    }

    @Test
    void get_home_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/home")
                        .accept(MediaType.APPLICATION_JSON)
        );


        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        // todayGames 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames", hasSize(greaterThanOrEqualTo(0)))); // 있거나 없음
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].gameId").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].gameStatus").isString());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.todayGames[0].stadiumName").isString());

        // winPredictions 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.winPredictions", hasSize(greaterThanOrEqualTo(0))));

        // boards 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards", hasSize(lessThanOrEqualTo(5))));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document); // RestDocs
    }
}