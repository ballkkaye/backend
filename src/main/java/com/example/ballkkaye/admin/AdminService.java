package com.example.ballkkaye.admin;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public User login(AdminRequest.LoginDTO reqDTO) {

        User userPS = userRepository.findByUsernameAndRole(reqDTO.getUsername(), reqDTO.getUserRole());

        if (userPS == null) {
            throw new RuntimeException("유저네임 혹은 비밀번호가 틀렸습니다");
        }

        if (!userPS.getPassword().equals(reqDTO.getPassword())) {
            throw new RuntimeException("유저네임 혹은 비밀번호가 틀렸습니다");
        }
        String accessToken = JwtUtil.create(userPS);
        System.out.println(accessToken);
        return userPS;
    }
}
