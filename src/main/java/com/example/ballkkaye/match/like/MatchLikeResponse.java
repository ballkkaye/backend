package com.example.ballkkaye.match.like;

import lombok.Data;

public class MatchLikeResponse {
    @Data
    public static class SaveDTO {
        private Integer likeId;
        private Integer likeCount;
        private Boolean isLiked;

        public SaveDTO(Integer likeId, Integer likeCount) {
            this.likeId = likeId;
            this.likeCount = likeCount;
            this.isLiked = true;
        }
    }

    @Data
    public static class DeleteDTO {
        private Integer likeCount;
        private Boolean isLiked;

        public DeleteDTO(Integer likeCount, Boolean isLiked) {
            this.likeCount = likeCount;
            this.isLiked = isLiked;
        }
    }
}
