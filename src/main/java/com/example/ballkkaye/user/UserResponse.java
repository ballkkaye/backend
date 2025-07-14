package com.example.ballkkaye.user;

import lombok.Data;

public class UserResponse {
    @Data
    public static class NaverVerifyDTO { // static 추가!
        private String resultcode;
        private String message;
        private NaverUserInfo response;

        @Data
        public static class NaverUserInfo {
            private String id;
            private String nickname;
            private String profile_image;
            private String age;
            private String gender;
            private String email;
            private String mobile;
            private String mobile_e164;
            private String name;
            private String birthday;
            private String birthyear;
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String name;
        private String nickname;
        private Integer teamId;
        private String phoneNumber;
        private String email;
        private String birthDate;
        private String gender;
        private String profileUrl;
        private String providerType;
        private String userRole;
        private String accessToken;
        private Boolean isNewUser;

        public LoginDTO(User user, String accessToken, Boolean isNewUser) {
            this.username = user.getUsername();
            this.name = user.getName();
            this.nickname = user.getNickname();
            this.teamId = user.getTeam() == null ? null : user.getTeam().getId();
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.birthDate = user.getBirthDate().toString();
            this.gender = user.getGender().toString();
            this.profileUrl = user.getProfileUrl();
            this.providerType = user.getProviderType().toString();
            this.userRole = user.getUserRole().toString();
            this.accessToken = accessToken;
            this.isNewUser = isNewUser;
        }
    }

    @Data
    public static class DTO {
        private String username;
        private String name;
        private String nickname;
        private Integer teamId;
        private String teamName;
        private String phoneNumber;
        private String email;
        private String birthDate;
        private String gender;
        private String profileUrl;
        private String providerType;
        private String userRole;

        public DTO(User user) {
            this.username = user.getUsername();
            this.name = user.getName();
            this.nickname = user.getNickname();
            this.teamId = user.getTeam() == null ? null : user.getTeam().getId();
            this.teamName = user.getTeam() == null ? null : user.getTeam().getTeamName();
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.birthDate = user.getBirthDate().toString();
            this.gender = user.getGender().toString();
            this.profileUrl = user.getProfileUrl();
            this.providerType = user.getProviderType().toString();
            this.userRole = user.getUserRole().toString();
        }
    }

    @Data
    public static class ScoreAndTierDTO {
        private Integer score;
        private String tier;
        private Integer userId;
        private String userNickname;

        public ScoreAndTierDTO(Integer score, String tier, Integer userId, String userNickname) {
            this.score = score;
            this.tier = tier;
            this.userId = userId;
            this.userNickname = userNickname;
        }
    }

    @Data
    public static class TokenDTO {
        private String accessToken;

        public TokenDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
