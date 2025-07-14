package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.JwtUtil;
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

    // 리프레시 토큰에 의한 fcm 토큰 갱신
    @PostMapping("/s/api/tokens/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid UserRequest.UpdateTokenDTO reqDTO) {
        var respDTO = userService.getRefreshAccessToken(reqDTO);
        return Resp.ok(respDTO);
    }

    private final UserRepository userRepository;

    // fcm 토큰 갱신용 임시 컨트롤러 - 하드코딩
    @PostMapping("/token/1")
    public ResponseEntity<?> updateFcmTokenHardcoded(@RequestBody UserRequest.FcmTokenUpdateDTO reqDTO) {
        User userPS = userRepository.findById(1).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 토큰이 비어있지 않으면 갱신
        if (reqDTO.getFcmToken() != null && !reqDTO.getFcmToken().isBlank()) {
            userService.updateFcmToken(userPS, reqDTO.getFcmToken());
        }

        // 새로운 JWT 발급
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }

    @GetMapping("/token/2")
    public ResponseEntity<?> token2() {
        User userPS = userRepository.findById(2).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }

    @GetMapping("/token/3")
    public ResponseEntity<?> token3() {
        User userPS = userRepository.findById(3).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }

    @GetMapping("/token/4")
    public ResponseEntity<?> token4() {
        User userPS = userRepository.findById(4).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }

    @GetMapping("/token/5")
    public ResponseEntity<?> token5() {
        User userPS = userRepository.findById(5).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }

    @GetMapping("/token/6")
    public ResponseEntity<?> token6() {
        User userPS = userRepository.findById(6).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
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