package com.example.ballkkaye.board;

import com.example.ballkkaye.board.image.BoardImageResponse;
import com.example.ballkkaye.team.TeamResponse;
import lombok.Data;

import java.util.List;

public class BoardResponse {

    @Data
    public static class SaveDTO {
        private Integer boardId;
        private String title;
        private Integer teamId;
        private List<BoardImageResponse.ItemDTO> imagesUrl;
        private String content;

        public SaveDTO(Board board, List<BoardImageResponse.ItemDTO> imagesUrl) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.teamId = board.getTeam().getId();
            this.content = board.getContent();
            this.imagesUrl = imagesUrl;
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer boardId;
        private String title;
        private Integer teamId;
        private List<BoardImageResponse.ItemDTO> imagesUrl;
        private String content;

        public UpdateDTO(Board board, List<BoardImageResponse.ItemDTO> imagesUrl) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.teamId = board.getTeam().getId();
            this.content = board.getContent();
            this.imagesUrl = imagesUrl;
        }
    }

    @Data
    public static class ItemDTO {
        private Integer boardId;
        private String title;
        private String nickname;
        private String relativeTime;
        private Integer teamId;
        private String teamName;
        private Integer replyCount;

        public ItemDTO(Integer boardId, String title, String nickname, String relativeTime, Integer teamId, String teamName, Integer replyCount) {
            this.boardId = boardId;
            this.title = title;
            this.nickname = nickname;
            this.relativeTime = relativeTime;
            this.teamId = teamId;
            this.teamName = teamName;
            this.replyCount = replyCount;
        }
    }

    @Data
    public static class ListDTO {
        private List<TeamResponse.ItemDTO> teams;
        private List<ItemDTO> items;

        public ListDTO(List<TeamResponse.ItemDTO> teams, List<ItemDTO> items) {
            this.teams = teams;
            this.items = items;
        }
    }
}
