package com.example.ballkkaye.user;

import lombok.Data;

public class UserRequest {
    @Data
    public static class SaveDTO {
        private String nickname;
        private Integer teamId;

        public SaveDTO(String nickname, Integer teamId) {
            this.nickname = nickname;
            this.teamId = teamId;
        }
    }
}
