package com.example.ballkkaye.integre;

import com.example.ballkkaye.MyRestDoc;
import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.stadium.Stadium;
import com.example.ballkkaye.stadium.StadiumRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.weather.weatherUltra.WeatherUltra;
import com.example.ballkkaye.weather.weatherUltra.WeatherUltraRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class WeatherUltraControllerTest extends MyRestDoc {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WeatherUltraRepository weatherUltraRepository;

    @Autowired
    private TodayGameRepository todayGameRepository;

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
        em.createNativeQuery("TRUNCATE TABLE weather_tb RESTART IDENTITY").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE weather_ultra_tb RESTART IDENTITY").executeUpdate();


        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();


        // given
        Stadium stadium1 = stadiumRepository.save(Stadium.builder()
                .stadiumName("잠실야구장")
                .location("서울 송파구")
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

        Team awayTeam1 = teamRepository.save(Team.builder()
                .stadium1(stadium5)
                .teamName("삼성 라이온즈")
                .logoUrl("/img/team/samsung.png")
                .ticketLink("https://www.samsunglions.com")
                .build());

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


        Random random = new Random();
        List<WeatherUltra> weatherList = new ArrayList<>();

        LocalDate today = LocalDate.now(); // 오늘 날짜 기준

        for (int hour = 6; hour <= 23; hour++) {
            LocalDateTime forecastTime = today.atTime(hour, 0);

            double temp = 28.0 + random.nextDouble() * 10; // 20.0 ~ 30.0도 사이
            double wind = 1.0 + random.nextDouble() * 3;   // 1.0 ~ 4.0 m/s
            double rain = random.nextDouble() * 20;        // 0 ~ 20 mm
            double rainPer = random.nextInt(51);           // 0 ~ 50%
            double humidity = 50 + random.nextInt(41);     // 50 ~ 90%

            WeatherUltra weather = WeatherUltra.builder()
                    .game(game1)
                    .stadium(stadium1)
                    .temperature(temp)
                    .windSpeed(wind)
                    .windDirection(WindDirection.values()[random.nextInt(WindDirection.values().length)])
                    .weatherCode(WFCD.values()[random.nextInt(WFCD.values().length)])
                    .rainPer(rainPer)
                    .humidityPer(humidity)
                    .forecastAt(Timestamp.valueOf(forecastTime))
                    .rainAmount(rain)
                    .build();

            weatherList.add(weather);
        }
        weatherUltraRepository.saveAll(weatherList);
    }


    @Test
    public void get_today_forecast_test() throws Exception {
        // given
        Integer stadiumId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/api/today/weather/{stadiumId}", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
        );


        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.location").exists());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].hour").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].temperature").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourly[0].weatherCode").isString());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourlyRain").isArray());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.hourlyRain[0].rainPer").isNumber());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.message").exists());
        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document); // RestDocs

    }
}
