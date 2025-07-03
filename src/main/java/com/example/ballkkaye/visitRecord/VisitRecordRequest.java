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
        private String imageString;


        public VisitRecord toEntity(User user, Game game, Team team) {
            return VisitRecord.builder()
                    .game(game)
                    .team(team)
                    .user(user)
                    .result(result)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .content(content)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private Result result;
        private String content;
        private String imageString;
    }

}
