package com.example.ballkkaye.weather.weatherUltra;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class WeatherUltraController {
    private final WeatherUltraService weatherUltraService;
    private final TodayGameRepository todayGameRepository;


    @GetMapping("/api/today/weather/{stadiumId}")
    public ResponseEntity<?> getTodayForecast(@PathVariable("stadiumId") Integer stadiumId) {
        // 경기 정보 조회
        TodayGame game = todayGameRepository.findByStadiumIdAndDate(stadiumId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("오늘 경기 정보가 없습니다."));

        Timestamp gameTime = game.getGameTime();
        
        WeatherUltraResponse.DTO respDTO = weatherUltraService.getTodayForecast(stadiumId, gameTime);
        return Resp.ok(respDTO);
    }
}
