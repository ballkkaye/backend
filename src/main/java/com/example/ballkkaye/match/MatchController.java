package com.example.ballkkaye.match;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchController {
    private final MatchService matchService;
    private final HttpSession session;

    // 매칭 글쓰기
    @PostMapping("/s/api/match")
    public ResponseEntity<?> save(@RequestBody @Valid MatchRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = matchService.save(sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }
}