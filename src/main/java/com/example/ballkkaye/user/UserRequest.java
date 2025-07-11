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
        private String fcmToken;

        public LoginDTO(String accessToken, String fcmToken) {
            this.accessToken = accessToken;
            this.fcmToken = fcmToken;
        }
    }

    @Data
    public static class UpdateDTO {
        private String nickname;
        private Integer teamId;
        private String ProfileImg;

        public UpdateDTO(String nickname, Integer teamId, String profileImg) {
            this.nickname = nickname;
            this.teamId = teamId;
            ProfileImg = profileImg;
        }
    }

    @Data
    public static class FcmTokenUpdateDTO {
        private String fcmToken;

        public FcmTokenUpdateDTO(String fcmToken) {
            this.fcmToken = fcmToken;
        }
    }
}
