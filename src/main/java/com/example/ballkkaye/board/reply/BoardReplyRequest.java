package com.example.ballkkaye.board.reply;

import jakarta.validation.constraints.Size;
import lombok.Data;

public class BoardReplyRequest {
    @Data
    public static class SaveDTO {
        private Integer parentReplyId;
        private Integer tagReplyId;
        @Size(max = 300, message = "내용은 300자 이내로 작성해주세요.")
        private String content;

        public SaveDTO(Integer parentReplyId, Integer tagReplyId, String content) {
            this.parentReplyId = parentReplyId;
            this.tagReplyId = tagReplyId;
            this.content = content;
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer tagReplyId;
        @Size(max = 300, message = "내용은 300자 이내로 작성해주세요.")
        private String content;

        public UpdateDTO(Integer tagReplyId, String content) {
            this.tagReplyId = tagReplyId;
            this.content = content;
        }
    }
}
