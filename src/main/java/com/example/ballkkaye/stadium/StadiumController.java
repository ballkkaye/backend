package com.example.ballkkaye.stadium;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StadiumController {
    private final StadiumService stadiumService;


    // 구장 선택 옵션박스 API
    @GetMapping("/api/stadiums")
    public ResponseEntity<?> getStadiumOptions() {
        List<StadiumResponse.ListDTO> stadiums = stadiumService.getStadiums();
        return Resp.ok(stadiums);
    }
}