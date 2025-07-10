package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

public class MatchRequest {

    @Data
    public static class SaveDTO {
        @NotNull(message = "경기 ID는 필수입니다.")
        private Integer gameId;

        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @NotNull(message = "최대 참가 인원은 필수입니다.")
        @Max(value = 10, message = "최대 참가 인원은 10명 이하여야 합니다.")
        private Integer maxParticipants;

        @NotNull(message = "희망 성별은 필수입니다.")
        private Gender preferredGender;

        @NotNull(message = "희망 나이는 필수입니다.")
        private Age preferredAge;

        @NotBlank(message = "제목은 필수 입력입니다.")
        private String title;

        @Size(max = 1000, message = "내용은 1000자 이내로 작성해주세요.")
        private String content;

        @NotNull(message = "같은 팀 여부 체크는 필수입니다.")
        private Boolean isSameTeam;

        public SaveDTO(Integer gameId, Integer teamId, Integer maxParticipants, Gender preferredGender, Age preferredAge, String title, String content, Boolean isSameTeam) {
            this.gameId = gameId;
            this.teamId = teamId;
            this.maxParticipants = maxParticipants;
            this.preferredGender = preferredGender;
            this.preferredAge = preferredAge;
            this.title = title;
            this.content = content;
            this.isSameTeam = isSameTeam;
        }
    }

    @Data
    public static class UpdateDTO {

        @NotNull(message = "경기 ID는 필수입니다.")
        private Integer gameId;

        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @NotNull(message = "최대 참가 인원은 필수입니다.")
        @Max(value = 10, message = "최대 참가 인원은 10명 이하여야 합니다.")
        private Integer maxParticipants;

        @NotNull(message = "희망 성별은 필수입니다.")
        private Gender preferredGender;

        @NotNull(message = "희망 나이는 필수입니다.")
        private Age preferredAge;

        @NotBlank(message = "제목은 필수 입력입니다.")
        private String title;

        @Size(max = 1000, message = "내용은 1000자 이내로 작성해주세요.")
        private String content;
        
        @NotNull(message = "같은 팀 여부 체크는 필수입니다.")
        private Boolean isSameTeam;

        public UpdateDTO(String title, String content, Integer gameId, Integer teamId, Boolean isSameTeam, Integer maxParticipants, Gender preferredGender, Age preferredAge) {
            this.title = title;
            this.content = content;
            this.gameId = gameId;
            this.teamId = teamId;
            this.isSameTeam = isSameTeam;
            this.maxParticipants = maxParticipants;
            this.preferredGender = preferredGender;
            this.preferredAge = preferredAge;
        }
    }
}