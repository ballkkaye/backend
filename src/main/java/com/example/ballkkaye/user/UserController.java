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

    // oauth로그인
    @PostMapping("/api/oauth/login")
    public ResponseEntity<?> naverOauthLogin(@RequestBody @Valid UserRequest.LoginDTO reqDTO) {
        var userInfo = userService.naverOauthLogin(reqDTO.getAccessToken());
        return Resp.ok(userInfo);
    }

    // 유저 추가정보 유저 응원팀 id + 유저 닉네임
    @PutMapping("/s/addtion-user-info")
    public ResponseEntity<?> additionalUserInfo(@PathVariable @Valid UserRequest.AdditionalInfoDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.additionalUserInfo(sessionUser, reqDTO);
        return null;
    }

    // 유저닉네임 중복체크
    @GetMapping("/api/check-nickname-available/{nickname}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("nickname") String nickname) {
        Map<String, Object> respDTO = userService.checkUsernameAvailable(nickname);
        return Resp.ok(respDTO);
    }
}