package com.example.ballkkaye.weather.weatherUltra;

import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.WeatherUtil;
import com.example.ballkkaye.common.enums.RainoutLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WeatherUltraService {
    private final WeatherUltraRepository weatherUltraRepository;


    public WeatherUltraResponse.DTO getTodayForecast(Integer stadiumId, Timestamp gameTime) {
        // 1. 특정 구장의 오늘 예보 데이터를 조회
        List<WeatherUltra> forecasts = weatherUltraRepository.findTodayForecastByStadiumId(stadiumId);

        if (forecasts.isEmpty()) {
            throw new ExceptionApi404("해당 구장의 예보 데이터가 존재하지 않습니다.");
        }

        // 예보에 포함된 구장의 위치 정보 (ex. 서울시 송파구 등)
        String location = forecasts.get(0).getStadium().getLocation();

        // 예보 리스트 구성용 DTO 컬렉션 초기화
        List<WeatherUltraResponse.DTO.HourlyWeatherDTO> hourlyWeatherList = new ArrayList<>();
        List<WeatherUltraResponse.DTO.HourlyRainPerDTO> hourlyRainList = new ArrayList<>();
        List<Double> rainoutScoreList = new ArrayList<>();

        for (WeatherUltra w : forecasts) {
            // 2-1. 어제 같은 시간대와의 온도 차이 계산
            Double diffFromYesterday;
            try {
                diffFromYesterday = WeatherUtil.calculateTemperatureDiffFromYesterday(w, weatherUltraRepository);
            } catch (Exception e) {
                diffFromYesterday = 0.0;  // 예외 발생 시 기본값
            }

            // 2-2. 시간별 날씨 및 강수 정보 DTO 생성
            hourlyWeatherList.add(new WeatherUltraResponse.DTO.HourlyWeatherDTO(w, diffFromYesterday));
            hourlyRainList.add(new WeatherUltraResponse.DTO.HourlyRainPerDTO(w));

            // 2-3. 시간별 우천취소 점수 계산 및 누적
            double rainoutScore = WeatherUtil.predictRainoutFromBoth(w, w.getRainPer());
            rainoutScoreList.add(rainoutScore);
        }

        // 3. 경기 시작 시간 기준 가장 가까운 예보를 찾음
        WeatherUltra closestForecast = forecasts.stream()
                .min(Comparator.comparing(f -> Math.abs(f.getForecastAt().getTime() - gameTime.getTime())))
                .orElse(null);

        // 4. 해당 예보의 우천취소 점수 계산
        double rainoutScore = 0.0;
        if (closestForecast != null) {
            rainoutScore = WeatherUtil.predictRainoutFromBoth(closestForecast, closestForecast.getRainPer());
        }

        // 5. 우천취소 점수 기반으로 Enum 메시지 도출
        RainoutLevel level = RainoutLevel.fromScore(rainoutScore);
        String comment = level.getMessage();

        // 6. 종합 DTO 반환 (위치 + 시간별 정보 + 강수 정보 + 코멘트)
        return new WeatherUltraResponse.DTO(location, hourlyWeatherList, hourlyRainList, comment);
    }
}



