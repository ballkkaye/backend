package com.example.ballkkaye.weatherUltra;


import com.example.ballkkaye.weather.weatherUltra.WeatherUltra;
import com.example.ballkkaye.weather.weatherUltra.WeatherUltraRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Import(WeatherUltraRepository.class)
@DataJpaTest
public class WeatherUltraRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private WeatherUltraRepository weatherUltraRepository;

    @Test
    public void findTodayForecastByStadiumId_test() {
        // given
        Integer stadiumId = 1;

        // when
        List<WeatherUltra> result = weatherUltraRepository.findTodayForecastByStadiumId(stadiumId);

        // eye
        System.out.println("오늘 예보 수: " + result.size());
        for (WeatherUltra w : result) {
            System.out.println(
                    String.format(
                            "시각: %s | 기온: %.1f°C | 습도: %.1f%% | 풍속: %.1f m/s | 풍향: %s | 날씨코드: %s | 강수확률: %.1f%% | 강수량: %.1f mm",
                            w.getForecastAt(),
                            w.getTemperature(),
                            w.getHumidityPer(),
                            w.getWindSpeed(),
                            w.getWindDirection().getName(),
                            w.getWeatherCode().getCode(),
                            w.getRainPer(),
                            w.getRainAmount()
                    )
            );
        }

    }

    @Test
    public void findYesterdayForecastSameHour_test() {
        // given
        Integer stadiumId = 1;
        // 어제 예보 데이터
        LocalDate yesterday = LocalDate.now().minusDays(1);


        // when
        Optional<WeatherUltra> result = weatherUltraRepository.findYesterdayForecastSameHour(
                stadiumId, 15, yesterday
        );

        // then (출력)
        if (result.isPresent()) {
            WeatherUltra found = result.get();
            System.out.println("조회된 예보 시각: " + found.getForecastAt());
            System.out.println("기온: " + found.getTemperature());
            System.out.println("습도: " + found.getHumidityPer());
            System.out.println("풍속: " + found.getWindSpeed());
            System.out.println("풍향: " + found.getWindDirection().getName());
            System.out.println("날씨코드: " + found.getWeatherCode().getCode());
            System.out.println("강수확률: " + found.getRainPer());
            System.out.println("강수량: " + found.getRainAmount());
        } else {
            System.out.println("해당 시간대의 예보가 존재하지 않습니다.");
        }
    }
}
