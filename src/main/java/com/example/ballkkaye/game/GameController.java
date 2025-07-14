package com.example.ballkkaye.game;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GameController {
    private final GameService gameService;

    @GetMapping("/s/api/games")
    public ResponseEntity<?> getGames(@RequestParam String date) {
        var respDTO = gameService.getGames(date);
        return Resp.ok(respDTO);
    }

    @GetMapping("/s/api/games/dates")
    public ResponseEntity<?> getCalendarGames(@RequestParam(required = false) String date) {
        var respDTO = gameService.getCalendarGames(date);
        return Resp.ok(respDTO);
    }
}