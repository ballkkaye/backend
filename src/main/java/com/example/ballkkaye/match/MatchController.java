package com.example.ballkkaye.match;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MatchController {
    private final MatchService matchService;
    private final HttpSession session;

    // 매칭 글쓰기
    @PostMapping("/s/api/matches")
    public ResponseEntity<?> save(@RequestBody @Valid MatchRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = matchService.save(sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }

    // 매칭 글 목록
    @GetMapping("/s/api/matches")
    public ResponseEntity<?> getMatches(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Age age,
            @RequestParam(required = false) Integer teamId) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = matchService.getMatches(sessionUser, page, gender, age, teamId);
        return Resp.ok(respDTO);
    }

    // 매칭 글 상세
    @GetMapping("/s/api/matches/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = matchService.getDetail(id, sessionUser);
        return Resp.ok(respDTO);
    }
}