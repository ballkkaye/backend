package com.example.ballkkaye.player.hitterLineup.today;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TodayHitterLineupController {
    private final TodayHitterLineupService todayHitterLineUpService;

    @GetMapping("/api/today/hitter-lineup")
    public ResponseEntity<?> getDetailMatchup(
            @RequestParam Integer gameId,
            @RequestParam Integer teamId
    ) {
        TodayHitterLineupResponse.DTO respDTO = todayHitterLineUpService.getDetailMatchup(gameId, teamId);
        return Resp.ok(respDTO);
    }


}