package com.example.ballkkaye.admin;

import com.example.ballkkaye.common.enums.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class AdminRequest {
    @Data
    public static class LoginDTO {
        @NotEmpty(message = "아이디를 입력해주세요.")
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
        private UserRole userRole;
    }
}
