package com.example.ballkkaye.board;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        @NotBlank(message = "제목은 필수 입력입니다.")
        private String title;

        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @Size(max = 10, message = "이미지는 최대 10개까지 첨부할 수 있습니다.")
        private List<String> images;

        @Size(max = 1000, message = "내용은 1000자 이내로 작성해주세요.")
        private String content;

        public Board toEntity(User user, Team team) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .team(team)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        @NotEmpty(message = "제목은 필수 입력입니다.")
        private String title;


        @Min(value = 1, message = "팀 ID는 1 이상이어야 합니다.")
        @Max(value = 10, message = "팀 ID는 10 이하여야 합니다.")
        private Integer teamId;

        @Size(max = 1000, message = "내용은 1000자 이내로 작성해주세요.")
        private String content;

        private List<String> remainImageUrls;
        private List<String> newImages;

        public UpdateDTO(String title, Integer teamId, String content, List<String> remainImageUrls, List<String> newImages) {
            this.title = title;
            this.teamId = teamId;
            this.content = content;
            this.remainImageUrls = remainImageUrls;
            this.newImages = newImages;
        }
    }
}