package com.example.ballkkaye.board.like;

import lombok.Data;

public class BoardLikeResponse {
    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer userId;
        private Integer boardId;
        private Integer count;
        private Boolean isLike;

        public SaveDTO(BoardLike boardLike, Integer boardLikeCount) {
            this.id = boardLike.getId();
            this.userId = boardLike.getUser().getId();
            this.boardId = boardLike.getBoard().getId();
            this.count = boardLikeCount;
            this.isLike = true;
        }
    }

    @Data
    public static class DeleteDTO {
        private Integer count;
        private Boolean isLike;

        public DeleteDTO(Integer boardLikeCount) {
            this.count = boardLikeCount;
            this.isLike = false;
        }
    }
}
