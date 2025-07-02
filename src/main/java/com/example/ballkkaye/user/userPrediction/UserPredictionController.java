package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserPredictionController {

    private final UserPredictionService userPredictionService;

    // 나의 승부 예측 1-1
    @PostMapping("/api/user-predictions")
    public ResponseEntity<?> savePrediction(@RequestBody UserPredictionRequest.SaveDTO reqDTO) {
        UserPredictionResponse.SaveListDTO respDTO = userPredictionService.savePrediction(reqDTO);
        return Resp.ok(respDTO);
    }

    // 나의 승부 예측 1
    @GetMapping("/api/user-predictions/today")
    public ResponseEntity<?> TodayPredictions(@RequestParam Integer userId) {
        UserPredictionResponse.TodayListResponse respDTO = userPredictionService.TodayPredictions(userId);
        return Resp.ok(respDTO);
    }
}
