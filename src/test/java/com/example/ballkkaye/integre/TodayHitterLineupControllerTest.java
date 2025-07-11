package com.example.ballkkaye.integre;


import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye.common.enums.BroadcastChannel;
import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.common.enums.StadiumType;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.player.Player;
import com.example.ballkkaye.player.PlayerRepository;
import com.example.ballkkaye.player.hitterLineup.today.TodayHitterLineup;
import com.example.ballkkaye.player.hitterLineup.today.TodayHitterLineupRepository;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcherRepository;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.stadium.StadiumRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TodayHitterLineupControllerTest extends MyRestDoc {

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
    private PlayerRepository playerRepository;

    @Autowired
    private TodayStartingPitcherRepository todayStartingPitcherRepository;

    @Autowired
    private TodayHitterLineupRepository todayHitterLineupRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        // 외래키 무시 + 전 테이블 truncate
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        em.createNativeQuery("TRUNCATE TABLE visit_record_tb RESTART IDENTITY").executeUpdate();
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


        // given
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

        List<Player> pitehers = List.of(player1, player2, player3, player4, player5, player6, player7, player8, player9, player10);
        playerRepository.saveAll(pitehers);

        // 타자
        // homeTeam1 - LG 트윈스
        Player player11 = Player.builder().kboPlayerId(10101).name("홍창기").team(homeTeam1).build(); // 1번 타자
        Player player12 = Player.builder().kboPlayerId(10102).name("문보경").team(homeTeam1).build();
        Player player13 = Player.builder().kboPlayerId(10103).name("김현수").team(homeTeam1).build();
        Player player14 = Player.builder().kboPlayerId(10104).name("채은성").team(homeTeam1).build();
        Player player15 = Player.builder().kboPlayerId(10105).name("박동원").team(homeTeam1).build();
        Player player16 = Player.builder().kboPlayerId(10106).name("오스틴").team(homeTeam1).build();
        Player player17 = Player.builder().kboPlayerId(10107).name("신민재").team(homeTeam1).build();
        Player player18 = Player.builder().kboPlayerId(10108).name("이재원").team(homeTeam1).build();
        Player player19 = Player.builder().kboPlayerId(10109).name("허도환").team(homeTeam1).build(); // 포수

        List<Player> home1Hitters = List.of(
                player11, player12, player13, player14, player15,
                player16, player17, player18, player19
        );


        // awayTeam1 - 삼성 라이온즈
        Player player21 = Player.builder().kboPlayerId(20101).name("김현준").team(awayTeam1).build(); // 1번 타자
        Player player22 = Player.builder().kboPlayerId(20102).name("구자욱").team(awayTeam1).build();
        Player player23 = Player.builder().kboPlayerId(20103).name("피렐라").team(awayTeam1).build();
        Player player24 = Player.builder().kboPlayerId(20104).name("이원석").team(awayTeam1).build();
        Player player25 = Player.builder().kboPlayerId(20105).name("강민호").team(awayTeam1).build();
        Player player26 = Player.builder().kboPlayerId(20106).name("오재일").team(awayTeam1).build();
        Player player27 = Player.builder().kboPlayerId(20107).name("김지찬").team(awayTeam1).build();
        Player player28 = Player.builder().kboPlayerId(20108).name("김성윤").team(awayTeam1).build();
        Player player29 = Player.builder().kboPlayerId(20109).name("이재현").team(awayTeam1).build(); // 유격수

        List<Player> away1Hitters = List.of(
                player21, player22, player23, player24, player25,
                player26, player27, player28, player29
        );

        // homeTeam2 - 두산 베어스
        Player player31 = Player.builder().kboPlayerId(12001).name("정수빈").team(homeTeam2).build();
        Player player32 = Player.builder().kboPlayerId(12002).name("허경민").team(homeTeam2).build();
        Player player33 = Player.builder().kboPlayerId(12003).name("강승호").team(homeTeam2).build();
        Player player34 = Player.builder().kboPlayerId(12004).name("김재환").team(homeTeam2).build();
        Player player35 = Player.builder().kboPlayerId(12005).name("양석환").team(homeTeam2).build();
        Player player36 = Player.builder().kboPlayerId(12006).name("장승현").team(homeTeam2).build();
        Player player37 = Player.builder().kboPlayerId(12007).name("김대한").team(homeTeam2).build();
        Player player38 = Player.builder().kboPlayerId(12008).name("박계범").team(homeTeam2).build();
        Player player39 = Player.builder().kboPlayerId(12009).name("김인태").team(homeTeam2).build();

        List<Player> home2Hitters = List.of(
                player31, player32, player33, player34, player35,
                player36, player37, player38, player39
        );

        // awayTeam2 - 롯데 자이언츠
        Player player40 = Player.builder().kboPlayerId(22001).name("안권수").team(awayTeam2).build();
        Player player41 = Player.builder().kboPlayerId(22002).name("전준우").team(awayTeam2).build();
        Player player42 = Player.builder().kboPlayerId(22003).name("이학주").team(awayTeam2).build();
        Player player43 = Player.builder().kboPlayerId(22004).name("유강남").team(awayTeam2).build();
        Player player44 = Player.builder().kboPlayerId(22005).name("정훈").team(awayTeam2).build();
        Player player45 = Player.builder().kboPlayerId(22006).name("한동희").team(awayTeam2).build();
        Player player46 = Player.builder().kboPlayerId(22007).name("고승민").team(awayTeam2).build();
        Player player47 = Player.builder().kboPlayerId(22008).name("정보근").team(awayTeam2).build();
        Player player48 = Player.builder().kboPlayerId(22009).name("황성빈").team(awayTeam2).build();

        List<Player> away2Hitters = List.of(
                player40, player41, player42, player43, player44,
                player45, player46, player47, player48
        );

        // homeTeam3 - 키움 히어로즈
        Player player49 = Player.builder().kboPlayerId(13001).name("이정후").team(homeTeam3).build();
        Player player50 = Player.builder().kboPlayerId(13002).name("김혜성").team(homeTeam3).build();
        Player player51 = Player.builder().kboPlayerId(13003).name("송성문").team(homeTeam3).build();
        Player player52 = Player.builder().kboPlayerId(13004).name("김휘집").team(homeTeam3).build();
        Player player53 = Player.builder().kboPlayerId(13005).name("임지열").team(homeTeam3).build();
        Player player54 = Player.builder().kboPlayerId(13006).name("박찬혁").team(homeTeam3).build();
        Player player55 = Player.builder().kboPlayerId(13007).name("김수환").team(homeTeam3).build();
        Player player56 = Player.builder().kboPlayerId(13008).name("이용규").team(homeTeam3).build();
        Player player57 = Player.builder().kboPlayerId(13009).name("송우현").team(homeTeam3).build();

        List<Player> home3Hitters = List.of(
                player49, player50, player51, player52, player53,
                player54, player55, player56, player57
        );

        // awayTeam3 - 한화 이글스
        Player player58 = Player.builder().kboPlayerId(23001).name("하주석").team(awayTeam3).build();
        Player player59 = Player.builder().kboPlayerId(23002).name("노시환").team(awayTeam3).build();
        Player player60 = Player.builder().kboPlayerId(23003).name("최재훈").team(awayTeam3).build();
        Player player61 = Player.builder().kboPlayerId(23004).name("김인환").team(awayTeam3).build();
        Player player62 = Player.builder().kboPlayerId(23005).name("이원석").team(awayTeam3).build();
        Player player63 = Player.builder().kboPlayerId(23006).name("장운호").team(awayTeam3).build();
        Player player64 = Player.builder().kboPlayerId(23007).name("노수광").team(awayTeam3).build();
        Player player65 = Player.builder().kboPlayerId(23008).name("정은원").team(awayTeam3).build();
        Player player66 = Player.builder().kboPlayerId(23009).name("양성우").team(awayTeam3).build();

        List<Player> away3Hitters = List.of(
                player58, player59, player60, player61, player62,
                player63, player64, player65, player66
        );

        // homeTeam4 - SSG 랜더스
        Player player67 = Player.builder().kboPlayerId(14001).name("최지훈").team(homeTeam4).build();
        Player player68 = Player.builder().kboPlayerId(14002).name("최정").team(homeTeam4).build();
        Player player69 = Player.builder().kboPlayerId(14003).name("한유섬").team(homeTeam4).build();
        Player player70 = Player.builder().kboPlayerId(14004).name("추신수").team(homeTeam4).build();
        Player player71 = Player.builder().kboPlayerId(14005).name("오태곤").team(homeTeam4).build();
        Player player72 = Player.builder().kboPlayerId(14006).name("김성현").team(homeTeam4).build();
        Player player73 = Player.builder().kboPlayerId(14007).name("박성한").team(homeTeam4).build();
        Player player74 = Player.builder().kboPlayerId(14008).name("김민식").team(homeTeam4).build();
        Player player75 = Player.builder().kboPlayerId(14009).name("장지훈").team(homeTeam4).build();

        List<Player> home4Hitters = List.of(
                player67, player68, player69, player70, player71,
                player72, player73, player74, player75
        );

        // awayTeam4 - NC 다이노스
        Player player76 = Player.builder().kboPlayerId(24001).name("손아섭").team(awayTeam4).build();
        Player player77 = Player.builder().kboPlayerId(24002).name("박건우").team(awayTeam4).build();
        Player player78 = Player.builder().kboPlayerId(24003).name("양의지").team(awayTeam4).build();
        Player player79 = Player.builder().kboPlayerId(24004).name("도태훈").team(awayTeam4).build();
        Player player80 = Player.builder().kboPlayerId(24005).name("김주원").team(awayTeam4).build();
        Player player81 = Player.builder().kboPlayerId(24006).name("김성욱").team(awayTeam4).build();
        Player player82 = Player.builder().kboPlayerId(24007).name("박민우").team(awayTeam4).build();
        Player player83 = Player.builder().kboPlayerId(24008).name("서호철").team(awayTeam4).build();
        Player player84 = Player.builder().kboPlayerId(24009).name("마틴").team(awayTeam4).build();

        List<Player> away4Hitters = List.of(
                player76, player77, player78, player79, player80,
                player81, player82, player83, player84
        );

        // homeTeam5 - KIA 타이거즈
        Player player85 = Player.builder().kboPlayerId(15001).name("김도영").team(homeTeam5).build();
        Player player86 = Player.builder().kboPlayerId(15002).name("박찬호").team(homeTeam5).build();
        Player player87 = Player.builder().kboPlayerId(15003).name("소크라테스").team(homeTeam5).build();
        Player player88 = Player.builder().kboPlayerId(15004).name("최형우").team(homeTeam5).build();
        Player player89 = Player.builder().kboPlayerId(15005).name("나성범").team(homeTeam5).build();
        Player player90 = Player.builder().kboPlayerId(15006).name("이우성").team(homeTeam5).build();
        Player player91 = Player.builder().kboPlayerId(15007).name("한승택").team(homeTeam5).build();
        Player player92 = Player.builder().kboPlayerId(15008).name("류지혁").team(homeTeam5).build();
        Player player93 = Player.builder().kboPlayerId(15009).name("김선빈").team(homeTeam5).build();

        List<Player> home5Hitters = List.of(
                player85, player86, player87, player88, player89,
                player90, player91, player92, player93
        );

        // awayTeam5 - KT 위즈
        Player player94 = Player.builder().kboPlayerId(25001).name("김민혁").team(awayTeam5).build();
        Player player95 = Player.builder().kboPlayerId(25002).name("배정대").team(awayTeam5).build();
        Player player96 = Player.builder().kboPlayerId(25003).name("강백호").team(awayTeam5).build();
        Player player97 = Player.builder().kboPlayerId(25004).name("박병호").team(awayTeam5).build();
        Player player98 = Player.builder().kboPlayerId(25005).name("장성우").team(awayTeam5).build();
        Player player99 = Player.builder().kboPlayerId(25006).name("황재균").team(awayTeam5).build();
        Player player100 = Player.builder().kboPlayerId(25007).name("김상수").team(awayTeam5).build();
        Player player101 = Player.builder().kboPlayerId(25008).name("심우준").team(awayTeam5).build();
        Player player102 = Player.builder().kboPlayerId(25009).name("이호연").team(awayTeam5).build();

        List<Player> away5Hitters = List.of(
                player94, player95, player96, player97, player98,
                player99, player100, player101, player102
        );

        playerRepository.saveAll(home1Hitters);
        playerRepository.saveAll(away1Hitters);
        playerRepository.saveAll(home2Hitters);
        playerRepository.saveAll(away2Hitters);
        playerRepository.saveAll(home3Hitters);
        playerRepository.saveAll(away3Hitters);
        playerRepository.saveAll(home4Hitters);
        playerRepository.saveAll(away4Hitters);
        playerRepository.saveAll(home5Hitters);
        playerRepository.saveAll(away5Hitters);


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

        TodayHitterLineup lgHitter1 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player11) // 홍창기
                .todayHitterOrder(1)
                .position("우익수")
                .ab(310)
                .h(97)
                .avg(0.313)
                .seasonAvg(0.313)
                .ops(0.790)
                .build();

        TodayHitterLineup lgHitter2 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player12) // 문보경
                .todayHitterOrder(2)
                .position("3루수")
                .ab(280)
                .h(85)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.810)
                .build();

        TodayHitterLineup lgHitter3 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player13) // 김현수
                .todayHitterOrder(3)
                .position("좌익수")
                .ab(295)
                .h(88)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.850)
                .build();

        TodayHitterLineup lgHitter4 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player14) // 채은성
                .todayHitterOrder(4)
                .position("1루수")
                .ab(270)
                .h(81)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.860)
                .build();

        TodayHitterLineup lgHitter5 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player15) // 박동원
                .todayHitterOrder(5)
                .position("포수")
                .ab(260)
                .h(77)
                .avg(0.296)
                .seasonAvg(0.296)
                .ops(0.780)
                .build();

        TodayHitterLineup lgHitter6 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player16) // 오스틴
                .todayHitterOrder(6)
                .position("지명타자")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.870)
                .build();

        TodayHitterLineup lgHitter7 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player17) // 신민재
                .todayHitterOrder(7)
                .position("2루수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.760)
                .build();

        TodayHitterLineup lgHitter8 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player18) // 이재원
                .todayHitterOrder(8)
                .position("중견수")
                .ab(225)
                .h(68)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.750)
                .build();

        TodayHitterLineup lgHitter9 = TodayHitterLineup.builder()
                .game(game1)
                .team(homeTeam1)
                .player(player19) // 허도환
                .todayHitterOrder(9)
                .position("유격수")
                .ab(200)
                .h(60)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.720)
                .build();

        List<TodayHitterLineup> home1HitterLineup = List.of(
                lgHitter1, lgHitter2, lgHitter3, lgHitter4, lgHitter5,
                lgHitter6, lgHitter7, lgHitter8, lgHitter9
        );

        TodayHitterLineup samsungHitter1 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player21) // 김현준
                .todayHitterOrder(1)
                .position("중견수")
                .ab(310)
                .h(95)
                .avg(0.306)
                .seasonAvg(0.306)
                .ops(0.780)
                .build();

        TodayHitterLineup samsungHitter2 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player22) // 구자욱
                .todayHitterOrder(2)
                .position("우익수")
                .ab(300)
                .h(90)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.820)
                .build();

        TodayHitterLineup samsungHitter3 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player23) // 피렐라
                .todayHitterOrder(3)
                .position("좌익수")
                .ab(290)
                .h(88)
                .avg(0.303)
                .seasonAvg(0.303)
                .ops(0.840)
                .build();

        TodayHitterLineup samsungHitter4 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player24) // 이원석
                .todayHitterOrder(4)
                .position("3루수")
                .ab(275)
                .h(82)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.800)
                .build();

        TodayHitterLineup samsungHitter5 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player25) // 강민호
                .todayHitterOrder(5)
                .position("포수")
                .ab(260)
                .h(79)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        TodayHitterLineup samsungHitter6 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player26) // 오재일
                .todayHitterOrder(6)
                .position("1루수")
                .ab(250)
                .h(76)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.860)
                .build();

        TodayHitterLineup samsungHitter7 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player27) // 김지찬
                .todayHitterOrder(7)
                .position("2루수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup samsungHitter8 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player28) // 김성윤
                .todayHitterOrder(8)
                .position("지명타자")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.730)
                .build();

        TodayHitterLineup samsungHitter9 = TodayHitterLineup.builder()
                .game(game1)
                .team(awayTeam1)
                .player(player29) // 이재현
                .todayHitterOrder(9)
                .position("유격수")
                .ab(220)
                .h(68)
                .avg(0.309)
                .seasonAvg(0.309)
                .ops(0.760)
                .build();

        List<TodayHitterLineup> away1HitterLineup = List.of(
                samsungHitter1, samsungHitter2, samsungHitter3, samsungHitter4, samsungHitter5,
                samsungHitter6, samsungHitter7, samsungHitter8, samsungHitter9
        );

        TodayHitterLineup doosanHitter1 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player31) // 정수빈
                .todayHitterOrder(1)
                .position("중견수")
                .ab(305)
                .h(92)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.790)
                .build();

        TodayHitterLineup doosanHitter2 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player32) // 허경민
                .todayHitterOrder(2)
                .position("3루수")
                .ab(295)
                .h(89)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.770)
                .build();

        TodayHitterLineup doosanHitter3 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player33) // 강승호
                .todayHitterOrder(3)
                .position("2루수")
                .ab(285)
                .h(85)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.740)
                .build();

        TodayHitterLineup doosanHitter4 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player34) // 김재환
                .todayHitterOrder(4)
                .position("좌익수")
                .ab(270)
                .h(83)
                .avg(0.307)
                .seasonAvg(0.307)
                .ops(0.810)
                .build();

        TodayHitterLineup doosanHitter5 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player35) // 양석환
                .todayHitterOrder(5)
                .position("1루수")
                .ab(260)
                .h(80)
                .avg(0.308)
                .seasonAvg(0.308)
                .ops(0.820)
                .build();

        TodayHitterLineup doosanHitter6 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player36) // 장승현
                .todayHitterOrder(6)
                .position("포수")
                .ab(250)
                .h(76)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.740)
                .build();

        TodayHitterLineup doosanHitter7 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player37) // 김대한
                .todayHitterOrder(7)
                .position("우익수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.730)
                .build();

        TodayHitterLineup doosanHitter8 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player38) // 박계범
                .todayHitterOrder(8)
                .position("유격수")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.710)
                .build();

        TodayHitterLineup doosanHitter9 = TodayHitterLineup.builder()
                .game(game2)
                .team(homeTeam2)
                .player(player39) // 김인태
                .todayHitterOrder(9)
                .position("지명타자")
                .ab(220)
                .h(66)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        List<TodayHitterLineup> home2HitterLineup = List.of(
                doosanHitter1, doosanHitter2, doosanHitter3, doosanHitter4, doosanHitter5,
                doosanHitter6, doosanHitter7, doosanHitter8, doosanHitter9
        );

        TodayHitterLineup lotteHitter1 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player40) // 안권수
                .todayHitterOrder(1)
                .position("중견수")
                .ab(300)
                .h(90)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.770)
                .build();

        TodayHitterLineup lotteHitter2 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player41) // 전준우
                .todayHitterOrder(2)
                .position("좌익수")
                .ab(290)
                .h(87)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.800)
                .build();

        TodayHitterLineup lotteHitter3 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player42) // 이학주
                .todayHitterOrder(3)
                .position("유격수")
                .ab(285)
                .h(84)
                .avg(0.295)
                .seasonAvg(0.295)
                .ops(0.750)
                .build();

        TodayHitterLineup lotteHitter4 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player43) // 유강남
                .todayHitterOrder(4)
                .position("포수")
                .ab(275)
                .h(80)
                .avg(0.291)
                .seasonAvg(0.291)
                .ops(0.740)
                .build();

        TodayHitterLineup lotteHitter5 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player44) // 정훈
                .todayHitterOrder(5)
                .position("1루수")
                .ab(265)
                .h(78)
                .avg(0.294)
                .seasonAvg(0.294)
                .ops(0.760)
                .build();

        TodayHitterLineup lotteHitter6 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player45) // 한동희
                .todayHitterOrder(6)
                .position("3루수")
                .ab(255)
                .h(76)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.780)
                .build();

        TodayHitterLineup lotteHitter7 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player46) // 고승민
                .todayHitterOrder(7)
                .position("우익수")
                .ab(245)
                .h(73)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.760)
                .build();

        TodayHitterLineup lotteHitter8 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player47) // 정보근
                .todayHitterOrder(8)
                .position("지명타자")
                .ab(235)
                .h(71)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.750)
                .build();

        TodayHitterLineup lotteHitter9 = TodayHitterLineup.builder()
                .game(game2)
                .team(awayTeam2)
                .player(player48) // 황성빈
                .todayHitterOrder(9)
                .position("2루수")
                .ab(225)
                .h(69)
                .avg(0.307)
                .seasonAvg(0.307)
                .ops(0.770)
                .build();

        List<TodayHitterLineup> away2HitterLineup = List.of(
                lotteHitter1, lotteHitter2, lotteHitter3, lotteHitter4, lotteHitter5,
                lotteHitter6, lotteHitter7, lotteHitter8, lotteHitter9
        );

        TodayHitterLineup kiwoomHitter1 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player49) // 이정후
                .todayHitterOrder(1)
                .position("중견수")
                .ab(310)
                .h(97)
                .avg(0.313)
                .seasonAvg(0.313)
                .ops(0.850)
                .build();

        TodayHitterLineup kiwoomHitter2 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player50) // 김혜성
                .todayHitterOrder(2)
                .position("2루수")
                .ab(300)
                .h(92)
                .avg(0.307)
                .seasonAvg(0.307)
                .ops(0.810)
                .build();

        TodayHitterLineup kiwoomHitter3 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player51) // 송성문
                .todayHitterOrder(3)
                .position("3루수")
                .ab(290)
                .h(89)
                .avg(0.307)
                .seasonAvg(0.307)
                .ops(0.790)
                .build();

        TodayHitterLineup kiwoomHitter4 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player52) // 김휘집
                .todayHitterOrder(4)
                .position("유격수")
                .ab(280)
                .h(85)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        TodayHitterLineup kiwoomHitter5 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player53) // 임지열
                .todayHitterOrder(5)
                .position("1루수")
                .ab(270)
                .h(82)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.780)
                .build();

        TodayHitterLineup kiwoomHitter6 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player54) // 박찬혁
                .todayHitterOrder(6)
                .position("우익수")
                .ab(260)
                .h(78)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup kiwoomHitter7 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player55) // 김수환
                .todayHitterOrder(7)
                .position("좌익수")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.760)
                .build();

        TodayHitterLineup kiwoomHitter8 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player56) // 이용규
                .todayHitterOrder(8)
                .position("지명타자")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.740)
                .build();

        TodayHitterLineup kiwoomHitter9 = TodayHitterLineup.builder()
                .game(game3)
                .team(homeTeam3)
                .player(player57) // 송우현
                .todayHitterOrder(9)
                .position("포수")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.720)
                .build();

        List<TodayHitterLineup> home3HitterLineup = List.of(
                kiwoomHitter1, kiwoomHitter2, kiwoomHitter3, kiwoomHitter4, kiwoomHitter5,
                kiwoomHitter6, kiwoomHitter7, kiwoomHitter8, kiwoomHitter9
        );

        TodayHitterLineup hanwhaHitter1 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player58) // 하주석
                .todayHitterOrder(1)
                .position("2루수")
                .ab(310)
                .h(96)
                .avg(0.310)
                .seasonAvg(0.310)
                .ops(0.760)
                .build();

        TodayHitterLineup hanwhaHitter2 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player59) // 노시환
                .todayHitterOrder(2)
                .position("3루수")
                .ab(300)
                .h(90)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.800)
                .build();

        TodayHitterLineup hanwhaHitter3 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player60) // 최재훈
                .todayHitterOrder(3)
                .position("포수")
                .ab(290)
                .h(87)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.780)
                .build();

        TodayHitterLineup hanwhaHitter4 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player61) // 김인환
                .todayHitterOrder(4)
                .position("1루수")
                .ab(275)
                .h(83)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.790)
                .build();

        TodayHitterLineup hanwhaHitter5 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player62) // 이원석
                .todayHitterOrder(5)
                .position("지명타자")
                .ab(260)
                .h(80)
                .avg(0.308)
                .seasonAvg(0.308)
                .ops(0.770)
                .build();

        TodayHitterLineup hanwhaHitter6 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player63) // 장운호
                .todayHitterOrder(6)
                .position("좌익수")
                .ab(250)
                .h(76)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.740)
                .build();

        TodayHitterLineup hanwhaHitter7 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player64) // 노수광
                .todayHitterOrder(7)
                .position("중견수")
                .ab(245)
                .h(73)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.710)
                .build();

        TodayHitterLineup hanwhaHitter8 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player65) // 정은원
                .todayHitterOrder(8)
                .position("유격수")
                .ab(235)
                .h(70)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.720)
                .build();

        TodayHitterLineup hanwhaHitter9 = TodayHitterLineup.builder()
                .game(game3)
                .team(awayTeam3)
                .player(player66) // 양성우
                .todayHitterOrder(9)
                .position("우익수")
                .ab(225)
                .h(68)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.715)
                .build();

        List<TodayHitterLineup> away3HitterLineup = List.of(
                hanwhaHitter1, hanwhaHitter2, hanwhaHitter3, hanwhaHitter4, hanwhaHitter5,
                hanwhaHitter6, hanwhaHitter7, hanwhaHitter8, hanwhaHitter9
        );

        TodayHitterLineup ssgHitter1 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player67) // 최지훈
                .todayHitterOrder(1)
                .position("중견수")
                .ab(320)
                .h(98)
                .avg(0.306)
                .seasonAvg(0.306)
                .ops(0.790)
                .build();

        TodayHitterLineup ssgHitter2 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player68) // 최정
                .todayHitterOrder(2)
                .position("3루수")
                .ab(310)
                .h(93)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.810)
                .build();

        TodayHitterLineup ssgHitter3 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player69) // 한유섬
                .todayHitterOrder(3)
                .position("우익수")
                .ab(305)
                .h(91)
                .avg(0.298)
                .seasonAvg(0.298)
                .ops(0.805)
                .build();

        TodayHitterLineup ssgHitter4 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player70) // 추신수
                .todayHitterOrder(4)
                .position("지명타자")
                .ab(290)
                .h(87)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.840)
                .build();

        TodayHitterLineup ssgHitter5 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player71) // 오태곤
                .todayHitterOrder(5)
                .position("좌익수")
                .ab(275)
                .h(84)
                .avg(0.305)
                .seasonAvg(0.305)
                .ops(0.780)
                .build();

        TodayHitterLineup ssgHitter6 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player72) // 김성현
                .todayHitterOrder(6)
                .position("2루수")
                .ab(260)
                .h(79)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        TodayHitterLineup ssgHitter7 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player73) // 박성한
                .todayHitterOrder(7)
                .position("유격수")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.760)
                .build();

        TodayHitterLineup ssgHitter8 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player74) // 김민식
                .todayHitterOrder(8)
                .position("포수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup ssgHitter9 = TodayHitterLineup.builder()
                .game(game4)
                .team(homeTeam4)
                .player(player75) // 장지훈
                .todayHitterOrder(9)
                .position("1루수")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        List<TodayHitterLineup> home4HitterLineup = List.of(
                ssgHitter1, ssgHitter2, ssgHitter3, ssgHitter4, ssgHitter5,
                ssgHitter6, ssgHitter7, ssgHitter8, ssgHitter9
        );

        TodayHitterLineup ncHitter1 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player76) // 손아섭
                .todayHitterOrder(1)
                .position("우익수")
                .ab(320)
                .h(102)
                .avg(0.319)
                .seasonAvg(0.319)
                .ops(0.810)
                .build();

        TodayHitterLineup ncHitter2 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player77) // 박건우
                .todayHitterOrder(2)
                .position("중견수")
                .ab(310)
                .h(97)
                .avg(0.313)
                .seasonAvg(0.313)
                .ops(0.820)
                .build();

        TodayHitterLineup ncHitter3 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player78) // 양의지
                .todayHitterOrder(3)
                .position("포수")
                .ab(300)
                .h(95)
                .avg(0.317)
                .seasonAvg(0.317)
                .ops(0.890)
                .build();

        TodayHitterLineup ncHitter4 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player79) // 도태훈
                .todayHitterOrder(4)
                .position("1루수")
                .ab(285)
                .h(88)
                .avg(0.309)
                .seasonAvg(0.309)
                .ops(0.860)
                .build();

        TodayHitterLineup ncHitter5 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player80) // 김주원
                .todayHitterOrder(5)
                .position("유격수")
                .ab(270)
                .h(82)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.780)
                .build();

        TodayHitterLineup ncHitter6 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player81) // 김성욱
                .todayHitterOrder(6)
                .position("좌익수")
                .ab(260)
                .h(79)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        TodayHitterLineup ncHitter7 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player82) // 박민우
                .todayHitterOrder(7)
                .position("2루수")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.760)
                .build();

        TodayHitterLineup ncHitter8 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player83) // 서호철
                .todayHitterOrder(8)
                .position("3루수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup ncHitter9 = TodayHitterLineup.builder()
                .game(game4)
                .team(awayTeam4)
                .player(player84) // 마틴
                .todayHitterOrder(9)
                .position("지명타자")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        List<TodayHitterLineup> away4HitterLineup = List.of(
                ncHitter1, ncHitter2, ncHitter3, ncHitter4, ncHitter5,
                ncHitter6, ncHitter7, ncHitter8, ncHitter9
        );

        TodayHitterLineup kiaHitter1 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player85) // 김도영
                .todayHitterOrder(1)
                .position("3루수")
                .ab(310)
                .h(96)
                .avg(0.310)
                .seasonAvg(0.310)
                .ops(0.830)
                .build();

        TodayHitterLineup kiaHitter2 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player86) // 박찬호
                .todayHitterOrder(2)
                .position("유격수")
                .ab(300)
                .h(91)
                .avg(0.303)
                .seasonAvg(0.303)
                .ops(0.790)
                .build();

        TodayHitterLineup kiaHitter3 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player87) // 소크라테스
                .todayHitterOrder(3)
                .position("중견수")
                .ab(295)
                .h(89)
                .avg(0.302)
                .seasonAvg(0.302)
                .ops(0.810)
                .build();

        TodayHitterLineup kiaHitter4 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player88) // 최형우
                .todayHitterOrder(4)
                .position("좌익수")
                .ab(280)
                .h(85)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.840)
                .build();

        TodayHitterLineup kiaHitter5 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player89) // 나성범
                .todayHitterOrder(5)
                .position("우익수")
                .ab(270)
                .h(81)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.850)
                .build();

        TodayHitterLineup kiaHitter6 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player90) // 이우성
                .todayHitterOrder(6)
                .position("1루수")
                .ab(260)
                .h(79)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.810)
                .build();

        TodayHitterLineup kiaHitter7 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player91) // 한승택
                .todayHitterOrder(7)
                .position("포수")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup kiaHitter8 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player92) // 류지혁
                .todayHitterOrder(8)
                .position("2루수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.740)
                .build();

        TodayHitterLineup kiaHitter9 = TodayHitterLineup.builder()
                .game(game5)
                .team(homeTeam5)
                .player(player93) // 김선빈
                .todayHitterOrder(9)
                .position("지명타자")
                .ab(230)
                .h(70)
                .avg(0.304)
                .seasonAvg(0.304)
                .ops(0.770)
                .build();

        List<TodayHitterLineup> home5HitterLineup = List.of(
                kiaHitter1, kiaHitter2, kiaHitter3, kiaHitter4, kiaHitter5,
                kiaHitter6, kiaHitter7, kiaHitter8, kiaHitter9
        );

        TodayHitterLineup ktHitter1 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player94) // 김민혁
                .todayHitterOrder(1)
                .position("중견수")
                .ab(310)
                .h(94)
                .avg(0.303)
                .seasonAvg(0.303)
                .ops(0.790)
                .build();

        TodayHitterLineup ktHitter2 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player95) // 배정대
                .todayHitterOrder(2)
                .position("좌익수")
                .ab(300)
                .h(90)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.800)
                .build();

        TodayHitterLineup ktHitter3 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player96) // 강백호
                .todayHitterOrder(3)
                .position("1루수")
                .ab(290)
                .h(87)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.870)
                .build();

        TodayHitterLineup ktHitter4 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player97) // 박병호
                .todayHitterOrder(4)
                .position("지명타자")
                .ab(280)
                .h(84)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.900)
                .build();

        TodayHitterLineup ktHitter5 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player98) // 장성우
                .todayHitterOrder(5)
                .position("포수")
                .ab(270)
                .h(81)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.820)
                .build();

        TodayHitterLineup ktHitter6 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player99) // 황재균
                .todayHitterOrder(6)
                .position("3루수")
                .ab(260)
                .h(78)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.780)
                .build();

        TodayHitterLineup ktHitter7 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player100) // 김상수
                .todayHitterOrder(7)
                .position("2루수")
                .ab(250)
                .h(75)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.750)
                .build();

        TodayHitterLineup ktHitter8 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player101) // 심우준
                .todayHitterOrder(8)
                .position("유격수")
                .ab(240)
                .h(72)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.740)
                .build();

        TodayHitterLineup ktHitter9 = TodayHitterLineup.builder()
                .game(game5)
                .team(awayTeam5)
                .player(player102) // 이호연
                .todayHitterOrder(9)
                .position("우익수")
                .ab(230)
                .h(69)
                .avg(0.300)
                .seasonAvg(0.300)
                .ops(0.730)
                .build();

        List<TodayHitterLineup> away5HitterLineup = List.of(
                ktHitter1, ktHitter2, ktHitter3, ktHitter4, ktHitter5,
                ktHitter6, ktHitter7, ktHitter8, ktHitter9
        );


        todayHitterLineupRepository.saveAll(home1HitterLineup);
        todayHitterLineupRepository.saveAll(away1HitterLineup);
        todayHitterLineupRepository.saveAll(home2HitterLineup);
        todayHitterLineupRepository.saveAll(away2HitterLineup);
        todayHitterLineupRepository.saveAll(home3HitterLineup);
        todayHitterLineupRepository.saveAll(away3HitterLineup);
        todayHitterLineupRepository.saveAll(home4HitterLineup);
        todayHitterLineupRepository.saveAll(away4HitterLineup);
        todayHitterLineupRepository.saveAll(home5HitterLineup);
        todayHitterLineupRepository.saveAll(away5HitterLineup);


    }


    @Test
    public void get_matchup_details_test() throws Exception {
        // given
        String gameId = "1";
        String teamId = "2";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/today/hitter-lineup")
                        .param("gameId", gameId)
                        .param("teamId", teamId)
                        .accept(MediaType.APPLICATION_JSON)  // 받을 데이터의 타입 명시

        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);


        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.season").value(2025));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.name").value("김건우"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.gameCount").value(14));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.result").value("6승 4패"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.qs").value(9));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.whip").value(1.15));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.era").value(3.12));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.profileUrl").value("https://example.com/player1.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hitters", hasSize(9)));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }


}
