package com.example.ballkkaye.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class UserRequest {
    @Data
    public static class AdditionalInfoDTO {

        private String nickname;

        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        public AdditionalInfoDTO(String nickname, Integer teamId) {
            this.nickname = nickname;
            this.teamId = teamId;
        }
    }

    @Data
    public static class LoginDTO {
        @NotBlank(message = "AccessToken은 필수입니다.")
        private String accessToken;

        public LoginDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Data
    public static class UpdateDTO {
        private String nickname;
        
        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @Pattern(
                regexp = "^https://.*\\.s3\\..*\\.amazonaws\\.com/.*$",
                message = "S3 URL만 허용됩니다."
        )
        private String ProfileImg;

        public UpdateDTO(String nickname, Integer teamId, String profileImg) {
            this.nickname = nickname;
            this.teamId = teamId;
            ProfileImg = profileImg;
        }
    }
}
