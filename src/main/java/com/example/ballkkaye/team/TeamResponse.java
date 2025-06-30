package com.example.ballkkaye.team;

import lombok.Data;

public class TeamResponse {

    @Data
    public static class ItemDTO {
        private Integer teamId;
        private String teamName;
        private String teamLogo;
        private Integer teamRank;

        public ItemDTO(Integer teamId, String teamName, String teamLogo, Integer teamRank) {
            this.teamId = teamId;
            this.teamName = teamName;
            this.teamLogo = teamLogo;
            this.teamRank = teamRank;
        }
    }
}
