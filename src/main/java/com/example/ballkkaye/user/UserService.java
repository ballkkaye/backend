package com.example.ballkkaye.user;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.GenerateNickname;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.*;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.userPrediction.UserPrediction;
import com.example.ballkkaye.user.userPrediction.UserPredictionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final TeamRepository teamRepository;
    private final UserPredictionRepository userPredictionRepository;

    @Transactional
    public Object naverOauthLogin(String accessToken, String fcmToken) {
        log.info("[NaverOauth] 네이버 로그인 요청 시작");

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
        log.debug("[NaverOauth] 응답 받은 유저: email={}, mobile={}", userInfo.getEmail(), userInfo.getMobile());

        // 1. 유저 중복 확인
        User userPS = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.findByPhoneNumber(userInfo.getMobile()).orElse(null));
        Boolean isNewUser = false;

        if (userPS == null) {
            log.info("[NaverOauth] 신규 유저입니다. 회원가입 진행: naverId={}", userInfo.getId());

            // 2. username 생성 (중복 방지용)
            String username = "NAVER_" + userInfo.getId(); // id 앞에거 잘라넣을지 생각해야됨

            // 3. birthDate 생성
            String fullBirth = userInfo.getBirthyear() + "-" + userInfo.getBirthday();
            LocalDate birthDate = LocalDate.parse(fullBirth);

            // 4. gender enum 넣기
            Gender gender = userInfo.getGender().equals("M") ? Gender.MALE : Gender.FEMALE;

            // 5. 비밀번호 생성
            String password = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());

            // 6. 닉네임 생성
            String nickname = GenerateNickname.create();

            // 7. user 객체 생성
            User user = User.builder()
                    .username(username)
                    .password(password) // 소셜 로그인용 고정
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
                    .fcmToken(fcmToken != null && !fcmToken.isBlank() ? fcmToken : null)
                    .predictionScore(0)
                    .predictionTier(PredictionTier.NONE)
                    .build();

            userRepository.save(user);
            log.info("[NaverOauth] 신규 유저 저장 완료: username={}", username);
            isNewUser = true;
            String myAccessToken = JwtUtil.create(user);

            return new UserResponse.LoginDTO(user, myAccessToken, isNewUser);
        }
        log.info("[NaverOauth] 기존 유저 로그인 성공: userId={}, email={}", userPS.getId(), userPS.getEmail());

        // 기존 유저라면 fcmToken 갱신
        if (fcmToken != null && !fcmToken.isBlank()) {
            log.debug("[NaverOauth] FCM 토큰 갱신 요청: userId={}", userPS.getId());
            userPS.updateFcmToken(fcmToken);
        }

        // 2. 로그인 처리 (토큰 발급 및 저장)
        String myAccessToken = JwtUtil.create(userPS);
        return new UserResponse.LoginDTO(userPS, myAccessToken, isNewUser);
    }

    // 유저 회원 가입 후 추가 정보 입력 유저 응원팀 id + 유저 닉네임
    @Transactional
    public Object getAdditionalUserInfo(User sessionUser, UserRequest.AdditionalInfoDTO reqDTO) {
        log.info("[회원 추가정보] 유저 추가정보 입력 요청: userId={}, teamId={}, nickname={}",
                sessionUser.getId(), reqDTO.getTeamId(), reqDTO.getNickname());

        // 1. 유저 존재하는지 검사
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("[회원 추가정보] 유저 조회 실패: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. 팀 존재 검사
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("[회원 추가정보] 팀 조회 실패: teamId={}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 닉네임 처리 공백이나 빈칸이 들어와도 null로
        String rawNickname = reqDTO.getNickname();
        String nickname = (rawNickname != null && !rawNickname.trim().isBlank()) ? rawNickname.trim() : null;

        // 닉네임이 null이 아닌 경우만 중복 검사
        if (nickname != null && userRepository.findByNickname(nickname).isPresent()) {
            log.warn("[회원 추가정보] 닉네임 중복: nickname={}", nickname);
            throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
        }

        // 닉네임과 팀 정보 업데이트 (nickname이 null이면 닉네임은 유지)
        userPS.additionalUserInfo(teamPS, nickname);
        log.info("[회원 추가정보] 추가정보 저장 완료: userId={}, teamId={}, nickname={}",
                userPS.getId(), teamPS.getId(), nickname);

        return new UserResponse.DTO(userPS);
    }

    // 유저 닉네임 중복체크
    public Map<String, Object> checkUserNicknameAvailable(String nickname) {
        log.debug("[닉네임 중복체크] 요청 nickname='{}'", nickname);

        Map<String, Object> respDTO = new HashMap<>();

        // 1. 유저네임 "" 또는 " " 일시 FALSE 반환
        if (nickname == null || nickname.trim().isEmpty()) {
            log.info("[닉네임 중복체크] 빈 문자열 또는 공백 입력");
            respDTO.put("available", false);
            return respDTO;
        }

        // 2. 닉네임으로 조회
        Optional<User> userOP = userRepository.findByNickname(nickname.trim());

        // 3. 가능 불가능 여부 반환
        if (userOP.isPresent()) {
            respDTO.put("available", false);
        } else {
            respDTO.put("available", true);
        }

        boolean exists = userRepository.findByNickname(nickname.trim()).isPresent();
        respDTO.put("available", !exists);

        log.info("[닉네임 중복체크] nickname='{}' → 중복 여부: {}", nickname, exists);
        return respDTO;
    }

    // 유저 정보 수정
    @Transactional
    public Object update(UserRequest.UpdateDTO reqDTO, User sessionUser) {
        log.debug("[유저 정보 수정] 요청: userId={}, nickname='{}', profileImg='{}', teamId={}",
                sessionUser.getId(), reqDTO.getNickname(), reqDTO.getProfileImg(), reqDTO.getTeamId());


        // 1. 유저 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("[유저 정보 수정] 유저 조회 실패: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. nickname, profileImg "" , " " 들어올 경우 null 로 치환
        String nickname = (reqDTO.getNickname() == null || reqDTO.getNickname().trim().isBlank())
                ? null : reqDTO.getNickname().trim();
        String profileImg = (reqDTO.getProfileImg() == null || reqDTO.getProfileImg().trim().isBlank())
                ? null : reqDTO.getProfileImg().trim();

        // 3. 닉네임 중복 검사 (null 아니고, 기존 닉네임과 다를 때만 검사)
        if (nickname != null && !nickname.equals(userPS.getNickname())) {
            if (userRepository.findByNickname(nickname).isPresent()) {
                log.info("[유저 정보 수정] 닉네임 중복: '{}'", nickname);
                throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
            }
        }

        // 4. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("[유저 정보 수정] 팀 조회 실패: teamId={}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 5. 업데이트 - updateUserInfo 해당 매개변수 null 일 경우 기존 자원 사용
        userPS.updateUserInfo(teamPS, nickname, profileImg);

        log.info("[유저 정보 수정] 수정 완료: userId={}, nickname='{}', profileImg='{}', teamId={}",
                userPS.getId(), nickname, profileImg, teamPS.getId());

        // 6. DTO 객체 생성하고 반환
        return new UserResponse.DTO(userPS);
    }


    // 유저 정보 조회
    public Object getUser(User sessionUser) {
        log.debug("[유저 정보 조회] userId={}", sessionUser.getId());

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }

    @Transactional
    public void updateScoreAndTier() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("[스코어 업데이트] {}일자 예측 점수 반영 시작", yesterday);

        // 1. 예측 찾아서
        List<UserPrediction> predictions = userPredictionRepository.findByGameDate(yesterday);

        // 2. for문 돌리고
        for (UserPrediction prediction : predictions) {

            // 예측 맞으면 점수 / 티어 업데이트
            if (prediction.getResult().equals(PredictionStatus.CORRECT)) {
                User user = prediction.getUser();
                user.updatePredictionScore(user.getPredictionScore() + 1);
                user.updatePredictionTier();
                log.info("[정답 반영] userId={}, 기존 점수={}, 갱신 점수={}", user.getId(), user.getPredictionScore(), user.getPredictionScore() + 1);
            }
        }
        log.info("[스코어 업데이트 완료] 총 반영된 예측 수: {}", predictions.size());

    }

    public Object getScoreAndTier(User sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        UserResponse.ScoreAndTierDTO respDTO = new UserResponse.ScoreAndTierDTO(
                user.getPredictionScore(),
                user.getPredictionTier().toString(),
                user.getId(),
                user.getNickname()
        );

        return respDTO;
    }
}