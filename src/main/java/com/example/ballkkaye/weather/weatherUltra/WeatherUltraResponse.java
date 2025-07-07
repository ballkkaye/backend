package com.example.ballkkaye.weather.weatherUltra;

import com.example.ballkkaye._core.util.WeatherUtil;
import lombok.Data;

import java.util.List;

public class WeatherUltraResponse {

    @Data
    public static class DTO {
        private String location;
        private List<HourlyWeatherDTO> hourly;
        private List<HourlyRainPerDTO> hourlyRain;
        private String message; // ex. "우천취소 가능성 높음"


        @Data
        public static class HourlyWeatherDTO {
            private int hour;
            private Double temperature;
            private String temperatureDiffFromYesterday; // 전날과 비교 온도차
            private String weatherCode;
            private Double humidity;
            private String windDirection;
            private Double windSpeed;

            public HourlyWeatherDTO(WeatherUltra weatherUltra, Double diffValue) {
                this.hour = weatherUltra.getForecastAt().toLocalDateTime().getHour();
                this.temperature = weatherUltra.getTemperature();
                this.temperatureDiffFromYesterday = WeatherUtil.formatTemperatureDiff(diffValue);
                this.weatherCode = weatherUltra.getWeatherCode().getCode();
                this.humidity = weatherUltra.getHumidityPer();
                this.windDirection = weatherUltra.getWindDirection().getName();
                this.windSpeed = weatherUltra.getWindSpeed();
            }
        }

        @Data
        public static class HourlyRainPerDTO {
            private int hour;
            private Double rainPer;
            private Double rainAmount;

            public HourlyRainPerDTO(WeatherUltra weatherUltra) {
                this.hour = weatherUltra.getForecastAt().toLocalDateTime().getHour();
                this.rainPer = weatherUltra.getRainPer();
                this.rainAmount = weatherUltra.getRainAmount();
            }
        }

        public DTO(String location, List<HourlyWeatherDTO> hourly, List<HourlyRainPerDTO> hourlyRain, String message) {
            this.location = location;
            this.hourly = hourly;
            this.hourlyRain = hourlyRain;
            this.message = message;
        }
    }

}
