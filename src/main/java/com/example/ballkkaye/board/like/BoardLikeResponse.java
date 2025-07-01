package com.example.ballkkaye.board.like;

import lombok.Data;

public class BoardLikeResponse {
    @Data
    public static class SaveDTO {
        private Integer id;
        private Integer userId;
        private Integer boardId;
        private Integer count;

        public SaveDTO(BoardLike boardLike, Integer boardLikeCount) {
            this.id = boardLike.getId();
            this.userId = boardLike.getUser().getId();
            this.boardId = boardLike.getBoard().getId();
            this.count = boardLikeCount;
        }
    }

    @Data
    public static class DeleteDTO {
        private Integer count;

        public DeleteDTO(Integer boardLikeCount) {
            this.count = boardLikeCount;
        }
    }

    @Data
    public static class DeleteDTO {
        private Integer boardLikeCount;

        public DeleteDTO(Integer boardLikeCount) {
            this.boardLikeCount = boardLikeCount;
        }
    }
}
