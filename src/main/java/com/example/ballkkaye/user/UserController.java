package com.example.ballkkaye.user;

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

    @PostMapping("/api/oauth/login") // Post로 해도 될 듯
    public ResponseEntity<?> naverOauthLogin(@RequestBody @Valid UserRequest.LoginDTO reqDTO) {
        var userInfo = userService.naverOauthLogin(reqDTO.getAccessToken());
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/s/myteam")
    public ResponseEntity<?> additionalUserInfo(@PathVariable @Valid UserRequest.AdditionalInfoDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.additionalUserInfo(sessionUser, reqDTO);
        return null;
    }
}