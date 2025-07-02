package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserPredictionController {

    private final UserPredictionService userPredictionService;

    @PostMapping("/api/user-predictions")
    public ResponseEntity<?> savePrediction(@RequestBody UserPredictionRequest.SaveDTO reqDTO) {
        List<UserPredictionResponse.SaveDTO> respDTO = userPredictionService.savePrediction(reqDTO);
        return Resp.ok(respDTO);
    }

    @GetMapping("/api/user-predictions/today")
    public ResponseEntity<?> getTodayPredictions(@RequestParam Integer userId) {
        UserPredictionResponse.TodayListResponse respDTO = userPredictionService.getTodayPredictions(userId);
        return Resp.ok(respDTO);
    }
}
