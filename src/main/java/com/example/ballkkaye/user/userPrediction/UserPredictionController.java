package com.example.ballkkaye.user.userPrediction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserPredictionController {

    private final UserPredictionService userPredictionService;

    @GetMapping("/api/predictions/today")
    public List<UserPredictionResponse.TodayGameDTO> getTodayGames(@RequestParam Integer userId) {
        return userPredictionService.getTodayGames(userId);
    }
}
