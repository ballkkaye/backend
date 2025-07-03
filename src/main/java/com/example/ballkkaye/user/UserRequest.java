package com.example.ballkkaye.user;

import lombok.Data;

public class UserRequest {
    @Data
    public static class AdditionalInfoDTO {
        private String nickname;
        private Integer teamId;

        public AdditionalInfoDTO(String nickname, Integer teamId) {
            this.nickname = nickname;
            this.teamId = teamId;
        }
    }

    @Data
    public static class LoginDTO {
        private String accessToken;

        public LoginDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Data
    public static class UpdateDTO {
        private String nickname;
        private Integer teamId;

        public UpdateDTO(String nickname, Integer teamId) {
            this.nickname = nickname;
            this.teamId = teamId;
        }
    }
}
