package com.example.ballkkaye.match.like;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MatchLikeController {
    private final MatchLikeService matchLikeService;
    private final HttpSession session;

    @PostMapping("/s/api/match/{id}/like")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = matchLikeService.save(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
