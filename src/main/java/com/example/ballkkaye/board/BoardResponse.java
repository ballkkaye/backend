package com.example.ballkkaye.board;

import com.example.ballkkaye.board.image.BoardImageResponse;
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
}
