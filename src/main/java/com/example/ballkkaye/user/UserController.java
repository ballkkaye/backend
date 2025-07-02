package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.Resp;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/api/oauth/login") // Post로 해도 될 듯
    public ResponseEntity<?> naverOauthLogin(@RequestParam("accessToken") String accessToken) {
        var userInfo = userService.naverOauthLogin(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String refreshToken) {
        var resp = userService.reissue(refreshToken);

        return Resp.ok(resp);
    }

    @PutMapping("/s/myteam")
    public ResponseEntity<?> selectMyTeam(@PathVariable @Valid UserRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.selectMyTeam(sessionUser, reqDTO);
        return null;
    }
}