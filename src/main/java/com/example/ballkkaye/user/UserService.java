package com.example.ballkkaye.user;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.GenerateNickname;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.ProviderType;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
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
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final TeamRepository teamRepository;

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

        // 1. 유저 중복 확인
        User userPS = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.findByPhoneNumber(userInfo.getMobile()).orElse(null));
        Boolean isNewUser = false;

        if (userPS == null) {
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
                    .build();

            userRepository.save(user);
            isNewUser = true;
            String myAccessToken = JwtUtil.create(user);

            return new UserResponse.LoginDTO(user, myAccessToken, isNewUser);
        }

        // 기존 유저라면 fcmToken 갱신
        if (fcmToken != null && !fcmToken.isBlank()) {
            userPS.updateFcmToken(fcmToken);
        }

        // 2. 로그인 처리 (토큰 발급 및 저장)
        String myAccessToken = JwtUtil.create(userPS);
        return new UserResponse.LoginDTO(userPS, myAccessToken, isNewUser);
    }

    // 유저 회원 가입 후 추가 정보 입력 유저 응원팀 id + 유저 닉네임
    @Transactional
    public Object getAdditionalUserInfo(User sessionUser, UserRequest.AdditionalInfoDTO reqDTO) {
        // 1. 유저 존재하는지 검사
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 2. 팀 존재 검사
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 3. 닉네임 처리 공백이나 빈칸이 들어와도 null로
        String rawNickname = reqDTO.getNickname();
        String nickname = (rawNickname != null && !rawNickname.trim().isBlank()) ? rawNickname.trim() : null;

        // 닉네임이 null이 아닌 경우만 중복 검사
        if (nickname != null && userRepository.findByNickname(nickname).isPresent()) {
            throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
        }

        // 닉네임과 팀 정보 업데이트 (nickname이 null이면 닉네임은 유지)
        userPS.additionalUserInfo(teamPS, nickname);

        return new UserResponse.DTO(userPS);
    }

    // 유저 닉네임 중복체크
    public Map<String, Object> checkUserNicknameAvailable(String nickname) {
        Map<String, Object> respDTO = new HashMap<>();

        // 1. 유저네임 "" 또는 " " 일시 FALSE 반환
        if (nickname == null || nickname.trim().isEmpty()) {
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

        return respDTO;
    }

    // 유저 정보 수정
    @Transactional
    public Object update(UserRequest.UpdateDTO reqDTO, User sessionUser) {
        // 1. 유저 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 2. nickname, profileImg "" , " " 들어올 경우 null 로 치환
        String nickname = (reqDTO.getNickname() == null || reqDTO.getNickname().trim().isBlank())
                ? null : reqDTO.getNickname().trim();
        String profileImg = (reqDTO.getProfileImg() == null || reqDTO.getProfileImg().trim().isBlank())
                ? null : reqDTO.getProfileImg().trim();

        // 3. 닉네임 중복 검사 (null 아니고, 기존 닉네임과 다를 때만 검사)
        if (nickname != null && !nickname.equals(userPS.getNickname())) {
            if (userRepository.findByNickname(nickname).isPresent()) {
                throw new ExceptionApi400("이미 존재하는 닉네임입니다.");
            }
        }

        // 4. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 5. 업데이트 - updateUserInfo 해당 매개변수 null 일 경우 기존 자원 사용
        userPS.updateUserInfo(teamPS, nickname, profileImg);

        // 6. DTO 객체 생성하고 반환
        return new UserResponse.DTO(userPS);
    }


    // 유저 정보 조회
    public Object getUser(User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }

    // 로그인 시 FcmToken 업데이트 - 하드코딩용
    @Transactional
    public void updateFcmToken(User sessionUser, String fcmToken) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        userPS.updateFcmToken(fcmToken); // 엔티티 내부의 setter 또는 메서드로 업데이트
    }
}