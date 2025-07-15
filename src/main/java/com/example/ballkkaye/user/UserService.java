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
        if (userInfo == null) {
            log.error("네이버 인증 실패 - accessToken 유효하지 않음");
            throw new ExceptionApi404("네이버 인증 정보를 가져올 수 없습니다.");
        }

        log.info("네이버 로그인 요청 - email: {}, name: {}", userInfo.getEmail(), userInfo.getName());

        // 1. 유저 중복 확인
        User userPS = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.findByPhoneNumber(userInfo.getMobile()).orElse(null));
        Boolean isNewUser = false;

        if (userPS == null) {
            log.info("신규 네이버 사용자 - email: {}, mobile: {}", userInfo.getEmail(), userInfo.getMobile());
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
            isNewUser = true;
            String myAccessToken = JwtUtil.create(user);

            log.info("신규 사용자 회원가입 완료 - username: {}, nickname: {}", user.getUsername(), user.getNickname());

            return new UserResponse.LoginDTO(user, myAccessToken, isNewUser);
        }

        // 기존 유저라면 fcmToken 갱신
        if (fcmToken != null && !fcmToken.isBlank()) {
            userPS.updateFcmToken(fcmToken);
            log.debug("기존 사용자 FCM 토큰 갱신 - userId: {}", userPS.getId());
        }

        // 2. 로그인 처리 (토큰 발급 및 저장)
        String myAccessToken = JwtUtil.create(userPS);
        log.info("기존 사용자 로그인 완료 - userId: {}, email: {}", userPS.getId(), userPS.getEmail());

        return new UserResponse.LoginDTO(userPS, myAccessToken, isNewUser);
    }

    // 유저 회원 가입 후 추가 정보 입력 유저 응원팀 id + 유저 닉네임
    @Transactional
    public Object getAdditionalUserInfo(User sessionUser, UserRequest.AdditionalInfoDTO reqDTO) {
        log.info("유저 추가 정보 입력 요청 - userId: {}, teamId: {}, nickname: '{}'",
                sessionUser.getId(), reqDTO.getTeamId(), reqDTO.getNickname());

        // 1. 유저 존재하는지 검사
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저로 추가 정보 요청 - userId: {}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // 2. 팀 존재 검사
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 팀 ID로 추가 정보 요청 - teamId: {}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 닉네임 처리 공백이나 빈칸이 들어와도 null로
        String rawNickname = reqDTO.getNickname();
        String nickname = (rawNickname != null && !rawNickname.trim().isBlank()) ? rawNickname.trim() : null;

        // 닉네임이 null이 아닌 경우만 중복 검사
        if (nickname != null && userRepository.findByNickname(nickname).isPresent()) {
            log.warn("닉네임 중복 시도 - userId: {}, 닉네임: '{}'", sessionUser.getId(), nickname);
            throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
        }

        // 닉네임과 팀 정보 업데이트 (nickname이 null이면 닉네임은 유지)
        userPS.additionalUserInfo(teamPS, nickname);

        log.info("유저 추가 정보 등록 완료 - userId: {}, 최종 nickname: '{}', team: {}",
                userPS.getId(), userPS.getNickname(), teamPS.getTeamName());

        return new UserResponse.DTO(userPS);
    }

    // 유저 닉네임 중복체크
    public Map<String, Object> checkUserNicknameAvailable(String nickname) {
        Map<String, Object> respDTO = new HashMap<>();

        // 1. 유저네임 "" 또는 " " 일시 FALSE 반환
        if (nickname == null || nickname.trim().isEmpty()) {
            log.info("닉네임 중복체크 요청 - 입력값 없음 또는 공백 ('{}')", nickname);
            respDTO.put("available", false);
            return respDTO;
        }

        // 2. 닉네임으로 조회
        Optional<User> userOP = userRepository.findByNickname(nickname.trim());

        boolean isAvailable = userOP.isEmpty();
        log.info("닉네임 중복체크 - nickname: '{}', available: {}", nickname.trim(), isAvailable);

        // 3. 가능 불가능 여부 반환
        if (userOP.isPresent()) {
            respDTO.put("available", false);
        } else {
            respDTO.put("available", true);
        }

        return respDTO;
    }

    // 유저 정보 수정
    @Transactional
    public Object update(UserRequest.UpdateDTO reqDTO, User sessionUser) {
        log.info("유저 정보 수정 요청 - userId: {}, reqNickname: '{}', reqTeamId: {}, reqProfileImg: '{}'",
                sessionUser.getId(), reqDTO.getNickname(), reqDTO.getTeamId(), reqDTO.getProfileImg());
        // 1. 유저 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저 정보 수정 시도 - userId: {}", sessionUser.getId());
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
                log.warn("닉네임 중복 - userId: {}, 입력 닉네임: '{}'", sessionUser.getId(), nickname);
                throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
            }
        }

        // 4. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("유효하지 않은 팀 ID로 수정 시도 - teamId: {}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 5. 업데이트 - updateUserInfo 해당 매개변수 null 일 경우 기존 자원 사용
        userPS.updateUserInfo(teamPS, nickname, profileImg);

        log.info("유저 정보 수정 완료 - userId: {}, 최종 nickname: '{}', team: {}, profileImg: {}",
                userPS.getId(), userPS.getNickname(), teamPS.getTeamName(), userPS.getProfileUrl());

        // 6. DTO 객체 생성하고 반환
        return new UserResponse.DTO(userPS);
    }


    // 유저 정보 조회
    public Object getUser(User sessionUser) {
        log.info("유저 정보 조회 요청 - userId: {}", sessionUser.getId());

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저 정보 조회 시도 - userId: {}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }

    @Transactional
    public void updateScoreAndTier() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("예측 점수 및 티어 업데이트 시작 - 대상 날짜: {}", yesterday);


        // 1. 예측 찾아서
        List<UserPrediction> predictions = userPredictionRepository.findByGameDate(yesterday);
        log.info("조회된 예측 수: {}", predictions.size());

        int updatedUserCount = 0;

        // 2. for문 돌리고
        for (UserPrediction prediction : predictions) {

            // 예측 맞으면 점수 / 티어 업데이트
            if (prediction.getResult().equals(PredictionStatus.CORRECT)) {
                User user = prediction.getUser();
                user.updatePredictionScore(user.getPredictionScore() + 1);
                user.updatePredictionTier();
                updatedUserCount++;
                log.debug("점수 업데이트 - userId: {}, oldScore: {}, newScore: {}", user.getId(), user.getPredictionScore(), user.getPredictionScore() + 1);
            }
        }

        log.info("예측 점수 업데이트 완료 - 총 갱신 유저 수: {}", updatedUserCount);
    }

    public Object getScoreAndTier(User sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("유저 점수/티어 조회 실패 - 존재하지 않는 유저 ID: {}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        UserResponse.ScoreAndTierDTO respDTO = new UserResponse.ScoreAndTierDTO(
                user.getPredictionScore(),
                user.getPredictionTier().toString(),
                user.getId(),
                user.getNickname()
        );

        Integer score = user.getPredictionScore();
        String tier = user.getPredictionTier().toString();

        log.info("유저 점수/티어 조회 - userId: {}, nickname: '{}', score: {}, tier: {}",
                sessionUser.getId(), user.getNickname(), score, tier);

        return respDTO;
    }
}