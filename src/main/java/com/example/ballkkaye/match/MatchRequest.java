package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.common.enums.Region;
import lombok.Data;

public class MatchRequest {

    @Data
    public static class SaveDTO {
        private Integer gameId;
        private Integer teamId;
        private Integer maxParticipants;
        private Gender preferredGender;
        private Age preferredAge;
        private Region preferredRegion;
        private String title;
        private String content;
        private Boolean isSameTeam;

        public SaveDTO(Integer gameId, Integer teamId, Integer maxParticipants, Gender preferredGender, Age preferredAge, Region preferredRegion, String title, String content, Boolean isSameTeam) {
            this.gameId = gameId;
            this.teamId = teamId;
            this.maxParticipants = maxParticipants;
            this.preferredGender = preferredGender;
            this.preferredAge = preferredAge;
            this.preferredRegion = preferredRegion;
            this.title = title;
            this.content = content;
            this.isSameTeam = isSameTeam;
        }
    }

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private Integer gameId;
        private Integer teamId;
        private Boolean isSameTeam;
        private Integer maxParticipants;
        private Gender preferredGender;
        private Age preferredAge;
        private Region preferredRegion;

        public UpdateDTO(String title, String content, Integer gameId, Integer teamId, Boolean isSameTeam, Integer maxParticipants, Gender preferredGender, Age preferredAge, Region preferredRegion) {
            this.title = title;
            this.content = content;
            this.gameId = gameId;
            this.teamId = teamId;
            this.isSameTeam = isSameTeam;
            this.maxParticipants = maxParticipants;
            this.preferredGender = preferredGender;
            this.preferredAge = preferredAge;
            this.preferredRegion = preferredRegion;
        }
    }
}
