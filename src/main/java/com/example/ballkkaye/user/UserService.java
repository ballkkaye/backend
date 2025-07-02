package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.GenerateNickname;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.ProviderType;
import com.example.ballkkaye.common.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public Object naverOauthLogin(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserResponse.NaverVerifyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                UserResponse.NaverVerifyDTO.class
        );
        UserResponse.NaverVerifyDTO.NaverUserInfo userInfo = response.getBody().getResponse();

        // 1. 유저 중복 확인
        User userPS = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.findByPhoneNumber(userInfo.getMobile()).orElse(null));

        if (userPS == null) {
            // 2. username 생성 (중복 방지용)
            String username = "NAVER_" + userInfo.getId(); // id 앞에거 잘라넣을지 생각해야됨

            // 3. birthDate 생성
            String fullBirth = userInfo.getBirthyear() + "-" + userInfo.getBirthday();
            LocalDate birthDate = LocalDate.parse(fullBirth);

            // 4. gender enum 넣기
            Gender gender = userInfo.getGender().equals("M") ? Gender.MALE : Gender.FEMALE;

            // 5.
            String nickname = GenerateNickname.create();

            // 5. user 객체 생성
            User user = User.builder()
                    .username(username)
                    .password(UUID.randomUUID().toString()) // 소셜 로그인용 고정
                    .name(userInfo.getName())
                    .nickname(nickname)
                    .team(null) // 팀은 null 또는 가입 이후 별도 입력
                    .email(userInfo.getEmail())
                    .birthDate(birthDate)
                    .phoneNumber(userInfo.getMobile())
                    .gender(gender)
                    .profileUrl(userInfo.getProfile_image())
                    .providerType(ProviderType.NAVER)
                    .userRole(UserRole.USER)
                    .build();
            String myRefreshToken = JwtUtil.createRefresh(user);
            String myAccessToken = JwtUtil.create(user);
            user.updateRefreshToken(myRefreshToken);
            userRepository.save(user);

            return new UserResponse.LoginDTO(user, myAccessToken, myRefreshToken);
        }
        // 2. 로그인 처리 (토큰 발급 및 저장)
        String myRefreshToken = JwtUtil.createRefresh(userPS);
        String myAccessToken = JwtUtil.create(userPS);
        userPS.updateRefreshToken(myRefreshToken);

        return new UserResponse.LoginDTO(userPS, myAccessToken, myRefreshToken);
    }

    @Transactional
    public Map<String, String> reissue(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new RuntimeException("RefreshToken이 올바르지 않습니다");
        }
        refreshToken = refreshToken.replace("Bearer ", "");
        User user = JwtUtil.verify(refreshToken);

        User userPS = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        if (!refreshToken.equals(userPS.getRefreshToken())) {
            throw new RuntimeException("RefreshToken이 일치하지 않습니다");
        }

        // 새 토큰들 발급
        String newAccess = JwtUtil.create(user);
        String newRefresh = JwtUtil.createRefresh(user);

        // DB 저장
        user.updateRefreshToken(newRefresh);
        userRepository.save(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccess);
        tokens.put("refreshToken", newRefresh);
        return tokens;
    }

    public void selectMyTeam(User sessionUser, UserRequest.@Valid SaveDTO reqDTO) {
    }
}