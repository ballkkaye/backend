package com.example.ballkkaye.board;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import lombok.Data;

import java.util.List;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        private String title;
        private Integer teamId;
        private List<String> images;
        private String content;

        public SaveDTO(String title, Integer teamId, List<String> images, String content) {
            this.title = title;
            this.teamId = teamId;
            this.images = images;
            this.content = content;
        }

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
}
