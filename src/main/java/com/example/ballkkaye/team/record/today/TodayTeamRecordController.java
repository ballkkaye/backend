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

    @GetMapping("/api/today/team-records")
    public ResponseEntity<?> getAllTodayTeamRecords() {
        List<TodayTeamRecordResponse.DTO> respDTO = todayTeamRecordService.getAllTeamRecords();
        return Resp.ok(respDTO);
    }
}