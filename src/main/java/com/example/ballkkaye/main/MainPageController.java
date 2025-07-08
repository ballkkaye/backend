package com.example.ballkkaye.main;

import com.example.ballkkaye._core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainPageController {
    private final MainPageService mainPageService;


    @GetMapping("/api/main-page")
    public ResponseEntity<?> getMainPage() {
        MainPageResponse.DTO respDTO = mainPageService.getMainPage();
        return Resp.ok(respDTO);
    }
}
