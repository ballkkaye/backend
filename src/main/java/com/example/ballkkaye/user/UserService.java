package com.example.ballkkaye.user;

import com.example.ballkkaye._core.util.GenerateNickname;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.ProviderType;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import jakarta.validation.Valid;
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
                    .build();
            userRepository.save(user);
            isNewUser = true;
            String myAccessToken = JwtUtil.create(user);

            return new UserResponse.LoginDTO(user, myAccessToken, isNewUser);
        }
        // 2. 로그인 처리 (토큰 발급 및 저장)
        String myAccessToken = JwtUtil.create(userPS);
        return new UserResponse.LoginDTO(userPS, myAccessToken, isNewUser);
    }

    @Transactional
    public Object additionalUserInfo(User sessionUser, UserRequest.@Valid AdditionalInfoDTO reqDTO) {
        // 유저 존재하는지 검사
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저lawk를 찾을 수 없습니다"));

        // req로 들어온 팀이 존재하는지 검사
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("해당 팀을 찾을 수 없습니다"));

        // 닉네임 중복 검사
        if (userRepository.findByNickname(reqDTO.getNickname()).isPresent()) {
            throw new RuntimeException("이미 존재하는 닉네임");
        }
        // updateNicknameAndTeam 함수 호출해서 응원팀, 닉네임 업데이트
        userPS.additionalUserInfo(teamPS, reqDTO.getNickname().trim());
        String myAccessToken = JwtUtil.create(userPS);

        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }

    public Map<String, Object> checkUsernameAvailable(String nickname) {
        Optional<User> userOP = userRepository.findByNickname(nickname);
        Map<String, Object> respDTO = new HashMap<>();

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
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 요청 닉네임이 내 닉네임과 같거나 비어있으면 기존 닉네임 유지
        if (!reqDTO.getNickname().equals(userPS.getNickname()) || reqDTO.getNickname().isEmpty()) {
            if (userRepository.findByNickname(reqDTO.getNickname()).isPresent()) {
                throw new RuntimeException("이미 존재하는 닉네임");
            }
        }
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("해당 팀을 찾을 수 없습니다"));

        userPS.updateUserInfo(teamPS, reqDTO.getNickname().trim(), reqDTO.getProfileImg());
        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }

    // 유저 정보 조회
    public Object getUser(User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        UserResponse.DTO respDTO = new UserResponse.DTO(userPS);
        return respDTO;
    }
}