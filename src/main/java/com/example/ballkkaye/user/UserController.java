package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.JwtUtil;
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
        var respDTO = userService.naverOauthLogin(reqDTO.getAccessToken());
        return Resp.ok(respDTO);
    }

    // 유저 추가정보 유저 응원팀 id + 유저 닉네임
    @PutMapping("/s/addtion-user-info")
    public ResponseEntity<?> additionalUserInfo(@RequestBody @Valid UserRequest.AdditionalInfoDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.additionalUserInfo(sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }

    // 유저닉네임 중복체크
    @GetMapping("/api/check-nickname-available/{nickname}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("nickname") String nickname) {
        Map<String, Object> respDTO = userService.checkUsernameAvailable(nickname);
        return Resp.ok(respDTO);
    }

    // 유저 정보 수정
    @PutMapping("/s/api/user")
    public ResponseEntity<?> update(@RequestBody @Valid UserRequest.UpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.update(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 유저 정보
    @GetMapping("/s/api/user")
    public ResponseEntity<?> getUser() {
        var sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = userService.getUser(sessionUser);
        return Resp.ok(respDTO);
    }

    private final UserRepository userRepository;

    @GetMapping("/token")
    public ResponseEntity<?> token() {
        User userPS = userRepository.findById(1).orElse(null);
        String newAccess = JwtUtil.create(userPS);
        return Resp.ok(newAccess);
    }
}