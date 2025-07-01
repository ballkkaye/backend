package com.example.ballkkaye.board.like;

import lombok.Data;

public class BoardLikeResponse {
    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer userId;
        private Integer boardId;

        public SaveDTO(BoardLike boardLike) {
            this.id = boardLike.getId();
            this.userId = boardLike.getUser().getId();
            this.boardId = boardLike.getBoard().getId();
        }
    }
}
