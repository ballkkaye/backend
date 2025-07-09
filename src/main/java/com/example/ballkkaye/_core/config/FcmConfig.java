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

    @PostConstruct
    public void initFirebase() {
        try {
            // 리소스를 클래스패스에서 읽어오기
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase/firebase-service-key.json");

            if (serviceAccount == null) {
                throw new ExceptionApi404("firebase-service-key.json 파일을 찾을 수 없습니다.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println(" FirebaseApp 초기화 성공");
            }

        } catch (IOException e) {
            throw new ExceptionApi400("FCM 초기화 실패: " + e.getMessage());
        }
    }
}