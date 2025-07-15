package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.Resp;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    // oauth로그인
    @PostMapping("/oauth/login")
    public ResponseEntity<?> naverOauthLogin(@RequestBody @Valid UserRequest.LoginDTO reqDTO) {
        var respDTO = userService.naverOauthLogin(reqDTO.getAccessToken(), reqDTO.getFcmToken());
        return Resp.ok(respDTO);
    }

    // 유저 회원 가입 후 추가 정보 입력 유저 응원팀 id + 유저 닉네임
    @PutMapping("/s/user/addtion-info")
    public ResponseEntity<?> getAdditionalUserInfo(@RequestBody @Valid UserRequest.AdditionalInfoDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.getAdditionalUserInfo(sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }

    // 유저닉네임 중복체크
    @GetMapping("/s/api/users/check-nickname-available/{nickname}")
    public ResponseEntity<?> checkUserNicknameAvailable(@PathVariable("nickname") String nickname) {
        Map<String, Object> respDTO = userService.checkUserNicknameAvailable(nickname);
        return Resp.ok(respDTO);
    }

    // 유저 정보 수정
    @PutMapping("/s/api/users")
    public ResponseEntity<?> update(@RequestBody @Valid UserRequest.UpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.update(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 유저 정보
    @GetMapping("/s/api/users")
    public ResponseEntity<?> getUser() {
        var sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.getUser(sessionUser);
        return Resp.ok(respDTO);
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void updateScoreAndTier() {
        userService.updateScoreAndTier();
    }

    @GetMapping("/s/api/users/tier")
    public ResponseEntity<?> getScoreAndTier() {
        var sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.getScoreAndTier(sessionUser);
        return Resp.ok(respDTO);
    }
}