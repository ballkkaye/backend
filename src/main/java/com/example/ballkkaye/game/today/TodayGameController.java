package com.example.ballkkaye.game.today;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodayGameController {

    private final TodayGameService todayGameService;

    // 승리예측 1
    @GetMapping("/s/api/today-games/prediction")
    public ResponseEntity<?> getTodayGamePredictions() {
        List<TodayGameResponse.PredictionDTO> respDTO = todayGameService.getTodayGamePredictions();
        return Resp.ok(respDTO);
    }

    // 날짜 기반 오늘 경기 목록 조회
    @GetMapping("/s/api/today-games")
    public ResponseEntity<?> getTodayGames(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<TodayGameResponse.ItemDTO> respDTO = todayGameService.getTodayGames(date);
        return Resp.ok(respDTO);
    }

    @GetMapping("/1")
    public String get() {
        User user = new User().builder()
                .id(1)
                .username("ssar123")
                .userRole(UserRole.USER)
                .build();
        String ac = JwtUtil.create(user);
        return ac;
    }
}
