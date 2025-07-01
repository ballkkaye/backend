package com.example.ballkkaye.board.like;

import lombok.Data;

public class BoardLikeResponse {
    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer userId;
        private Integer boardId;
        private Integer boardLikeCount;

        public SaveDTO(BoardLike boardLike, Integer boardLikeCount) {
            this.id = boardLike.getId();
            this.userId = boardLike.getUser().getId();
            this.boardId = boardLike.getBoard().getId();
            this.boardLikeCount = boardLikeCount;
        }
    }
}
