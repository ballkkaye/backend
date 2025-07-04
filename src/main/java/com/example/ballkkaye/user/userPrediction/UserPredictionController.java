package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserPredictionController {

    private final UserPredictionService userPredictionService;
    private final HttpSession session;

    @GetMapping("/api/predictions/today")
    public List<UserPredictionResponse.TodayGameDTO> getTodayGames(@RequestParam Integer userId) {
        return userPredictionService.getTodayGames(userId);
    }

    @GetMapping("/s/api/user-predictions")
    public ResponseEntity<?> findMyPredictions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<UserPredictionResponse.MyPredictionDTO> respDTO = userPredictionService.findMyPredictions(sessionUser.getId(), date);
        return Resp.ok(respDTO);
    }

}
