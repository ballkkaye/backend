package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.Resp;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/api/oauth/login")
    public ResponseEntity<?> naverOauthLogin(@RequestBody @Valid UserRequest.LoginDTO reqDTO) {
        var userInfo = userService.naverOauthLogin(reqDTO.getAccessToken());
        return Resp.ok(userInfo);
    }

    @PutMapping("/s/addtion-user-info")
    public ResponseEntity<?> additionalUserInfo(@PathVariable @Valid UserRequest.AdditionalInfoDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.additionalUserInfo(sessionUser, reqDTO);
        return null;
    }

    // 유저네임 중복체크
    @GetMapping("/api/check-username-available/{nickname}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("nickname") String nickname) {
        Map<String, Object> respDTO = userService.checkUsernameAvailable(nickname);
        return Resp.ok(respDTO);
    }
}