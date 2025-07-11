package com.example.ballkkaye.home;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {
    private final HomeService mainPageService;


    // 홈 화면
    @GetMapping("/api/home")
    public ResponseEntity<?> getHome() {
        HomeResponse.DTO respDTO = mainPageService.getHome();
        return Resp.ok(respDTO);
    }
}
