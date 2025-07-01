package com.example.ballkkaye.board.reply;

import lombok.Data;

public class BoardReplyRequest {
    @Data
    public static class SaveDTO {
        private Integer parentReplyId;
        private Integer tagReplyId;
        private String content;

        public SaveDTO(Integer parentReplyId, Integer tagReplyId, String content) {
            this.parentReplyId = parentReplyId;
            this.tagReplyId = tagReplyId;
            this.content = content;
        }
    }
}
