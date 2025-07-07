package com.example.ballkkaye.team.record.today;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodayTeamRecordController {
    private final TodayTeamRecordService todayTeamRecordService;

    // 팀 순위 호출
    @GetMapping("/s/api/today/team-records")
    public ResponseEntity<?> teamRecords() {
        List<TodayTeamRecordResponse.DTO> respDTO = todayTeamRecordService.teamRecords();
        return Resp.ok(respDTO);
    }
}