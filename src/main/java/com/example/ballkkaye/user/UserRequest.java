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
    }

    @Data
    public static class LoginDTO {
        @NotBlank(message = "AccessToken은 필수입니다.")
        private String accessToken;
        private String fcmToken;

        public LoginDTO(String accessToken, String fcmToken) {
            this.accessToken = accessToken;
            this.fcmToken = fcmToken;
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer userId;
        private String nickname;

        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @Pattern(
                regexp = "^https://.*\\.s3\\..*\\.amazonaws\\.com/.*$",
                message = "S3 URL만 허용됩니다."
        )
        private String profileImg;
    }

    @Data
    public static class FcmTokenUpdateDTO {
        private String fcmToken;

        public FcmTokenUpdateDTO(String fcmToken) {
            this.fcmToken = fcmToken;
        }
    }
}
