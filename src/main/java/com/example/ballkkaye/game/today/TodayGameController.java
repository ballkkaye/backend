package com.example.ballkkaye.game.today;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodayGameController {

    private final TodayGameService todayGameService;

    // 승리예측 1
    @GetMapping("/api/today-games/prediction")
    public ResponseEntity<?> getTodayPredictions() {
        List<TodayGameResponse.PredictionDTO> respDTO = todayGameService.getTodayGamePredictions();
        return Resp.ok(respDTO);
    }

    //
//    @GetMapping("/api/admin/today-games/init")
//    public ResponseEntity<?> initTodayGames() {
//        todayGameService.updateTodayGames();
//        return Resp.ok("오늘의 경기 갱신 완료");
//    }
}