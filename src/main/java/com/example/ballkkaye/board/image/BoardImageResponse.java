package com.example.ballkkaye.board.image;

import lombok.Data;

public class BoardImageResponse {
    @Data
    public static class ItemDTO {
        private Integer id;
        private String imageUrl;

        public ItemDTO(Integer id, String imageUrl) {
            this.id = id;
            this.imageUrl = imageUrl;
        }
    }
}
