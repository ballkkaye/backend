package com.example.ballkkaye.team;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TeamController {
    private final TeamService teamService;
    private final UserRepository userRepository; // 지워야함

    // 팀 목록 << 옵션박스용
    @GetMapping("/api/teams")
    public ResponseEntity<?> getTeams() {
        var respDTO = teamService.getTeams();
        return Resp.ok(respDTO);
    }
}