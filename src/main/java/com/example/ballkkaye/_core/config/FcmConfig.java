package com.example.ballkkaye._core.config;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FcmConfig {

    // Firebase Admin SDK를 초기화
    @PostConstruct
    public void initFirebase() {
        try {
            // [1] 클래스패스 기준으로 Firebase 서비스 계정 키 JSON 파일 로드 시도
            // - 보통 src/main/resources/firebase/firebase-service-key.json 에 위치
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase/firebase-service-key.json");

            // [2] 리소스 파일이 없으면 커스텀 404 예외 발생
            if (serviceAccount == null) {
                throw new ExceptionApi404("firebase-service-key.json 파일을 찾을 수 없습니다.");
            }

            // [3] FirebaseOptions 객체 생성
            // - 서비스 계정 키 기반으로 인증 구성
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // [4] 기존 FirebaseApp 이 초기화되어 있지 않은 경우에만 초기화 수행
            // - 여러 번 초기화 시도 방지
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp 초기화 성공");
            }

        } catch (IOException e) {
            // [5] JSON 파싱 실패 등 초기화 중 I/O 오류가 발생한 경우 커스텀 예외 발생
            throw new ExceptionApi400("FCM 초기화 실패: " + e.getMessage());
        }
    }
}