package com.example.ballkkaye.board.reply.like;

public class BoardReplyLikeResponse {
    @Data
    public static class SaveDTO {
        private BoardReplyLike boardReplyLike;
        private Integer boardReplyLikeCount;

        public SaveDTO(BoardReplyLike boardReplyLike, Integer boardLikeCount) {
            this.boardReplyLike = boardReplyLike;
            this.boardReplyLikeCount = boardLikeCount;
        }
    }
}
