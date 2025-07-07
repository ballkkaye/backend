package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Result;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import lombok.Data;

public class VisitRecordRequest {

    @Data
    public static class SaveDTO {
        private Integer gameId;
        private Integer teamId;
        private Result result;
        private String content;
        private String imgUrl;

        public SaveDTO(Integer gameId, Integer teamId, Result result, String content, String imgUrl) {
            this.gameId = gameId;
            this.teamId = teamId;
            this.result = result;
            this.content = content;
            this.imgUrl = imgUrl;
        }

        public VisitRecord toEntity(User user, Game game, Team team, String imgUrl) {
            return VisitRecord.builder()
                    .game(game)
                    .team(team)
                    .user(user)
                    .result(result)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .content(content)
                    .imgUrl(imgUrl)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private Result result;
        private String content;
        private String imgUrl;
    }
}
