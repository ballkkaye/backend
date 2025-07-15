package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserPredictionController {

    private final UserPredictionService userPredictionService;
    private final HttpSession session;

    // 유저 예측 화면 진입
    @GetMapping("/s/api/predictions/today")
    public ResponseEntity<?> todayGames(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<UserPredictionResponse.TodayGameDTO> respDTO = userPredictionService.todayGames(date, sessionUser.getId());
        return Resp.ok(respDTO);
    }

    // 유저 예측 완료 후 화면
    @GetMapping("/s/api/predictions")
    public ResponseEntity<?> findMyPredictions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<UserPredictionResponse.MyPredictionDTO> respDTO = userPredictionService.findMyPredictions(sessionUser.getId(), date);
        return Resp.ok(respDTO);
    }

    // 유저 예측 저장
    @PostMapping("/s/api/predictions")
    public ResponseEntity<?> save(@RequestBody List<UserPredictionRequest.SaveDTO> requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<UserPredictionRequest.SaveDTO> respDTO = userPredictionService.save(sessionUser.getId(), requestDTO);
        return Resp.ok(respDTO);
    }
}
