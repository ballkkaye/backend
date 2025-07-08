package com.example.ballkkaye._core.util;

import com.example.ballkkaye.weather.weatherUltra.WeatherUltra;
import com.example.ballkkaye.weather.weatherUltra.WeatherUltraRepository;

import java.time.LocalDate;
import java.util.Optional;

public class WeatherUtil {


    /**
     * 초단기 + 단기 예보 조합 기반 우천 취소 확률 계산
     */
    public static double predictRainoutFromBoth(WeatherUltra w, Double rainPer) {
        double rainAmount = w.getRainAmount() != null ? w.getRainAmount() : 0.0;
        double humidity = w.getHumidityPer() != null ? w.getHumidityPer() : 0.0;
        double wind = w.getWindSpeed() != null ? w.getWindSpeed() : 0.0;
        double rainProb = rainPer != null ? rainPer : 0.0;

        double score = rainAmount * 1.5 + humidity * 0.3 + wind * 0.2 + rainProb * 0.4;
        return Math.min(100.0, Math.round(score * 10.0) / 10.0);
    }


    /**
     * 주어진 오늘 예보 데이터와 같은 시간대의 어제 예보 데이터를 조회하여
     * 두 시간대의 온도 차이를 계산
     */
    public static Double calculateTemperatureDiffFromYesterday(
            WeatherUltra todayForecast, WeatherUltraRepository weatherUltraRepository
    ) {
        // 예보 데이터가 불완전한 경우 안전하게 0.0 반환
        if (todayForecast == null || todayForecast.getForecastAt() == null || todayForecast.getStadium() == null) {
            return 0.0;
        }

        // 비교 대상: 구장 ID, 시간 (시 단위), 어제 날짜
        Integer stadiumId = todayForecast.getStadium().getId();
        int forecastHour = todayForecast.getForecastAt().toLocalDateTime().getHour();
        LocalDate yesterday = todayForecast.getForecastAt().toLocalDateTime().toLocalDate().minusDays(1);

        // 어제 동일 구장, 동일 시간대의 예보 데이터를 조회
        Optional<WeatherUltra> optionalYesterday = weatherUltraRepository.findYesterdayForecastSameHour(
                stadiumId, forecastHour, yesterday
        );

        // 오늘과 어제의 온도를 비교
        Double todayTemp = todayForecast.getTemperature();
        Double yesterdayTemp = optionalYesterday.map(WeatherUltra::getTemperature).orElse(null);

        // 둘 중 하나라도 null이면 비교 불가 → 0.0
        if (todayTemp == null || yesterdayTemp == null) {
            return 0.0;
        }

        // 온도 차이 반환
        return todayTemp - yesterdayTemp;
    }

    /**
     * 온도 차이에 따른 텍스트 설명 (예: "어제보다 2.0°↑")
     */
    public static String formatTemperatureDiff(Double diff) {
        if (diff == null) return "정보 없음";
        if (diff > 0) return String.format("%.1f", diff) + "°↑";
        if (diff < 0) return String.format("%.1f", Math.abs(diff)) + "°↓";
        return "어제와 비슷";
    }


}
