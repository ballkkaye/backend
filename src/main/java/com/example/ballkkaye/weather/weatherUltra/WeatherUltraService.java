package com.example.ballkkaye.weather.weatherUltra;

import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.WeatherUtil;
import com.example.ballkkaye.common.enums.RainoutLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherUltraService {
    private final WeatherUltraRepository weatherUltraRepository;


    public WeatherUltraResponse.DTO getTodayForecast(Integer stadiumId, Timestamp gameTime) {
        log.info("오늘 초단기 예보 조회 요청 - stadiumId: {}, gameTime: {}", stadiumId, gameTime);

        // 1. 특정 구장의 오늘 예보 데이터를 조회
        List<WeatherUltra> forecasts = weatherUltraRepository.findTodayForecastByStadiumId(stadiumId);

        if (forecasts.isEmpty()) {
            log.warn("예보 데이터 없음 - stadiumId: {}", stadiumId);
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
                log.debug("어제 대비 온도차 계산 실패 - forecastAt: {}, stadiumId: {}", w.getForecastAt(), stadiumId);
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
            log.info("가장 근접한 예보 선택 - forecastAt: {}, rainoutScore: {}", closestForecast.getForecastAt(), rainoutScore);
        }

        // 5. 우천취소 점수 기반으로 Enum 메시지 도출
        RainoutLevel level = RainoutLevel.fromScore(rainoutScore);
        String comment = level.getMessage();

        log.info("우천취소 예측 완료 - stadiumId: {}, rainoutLevel: {}, message: {}", stadiumId, level.name(), comment);

        // 6. 종합 DTO 반환 (위치 + 시간별 정보 + 강수 정보 + 코멘트)
        return new WeatherUltraResponse.DTO(location, hourlyWeatherList, hourlyRainList, comment);
    }
}



