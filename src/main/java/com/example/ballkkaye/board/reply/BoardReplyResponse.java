package com.example.ballkkaye.board.reply;

import lombok.Data;

import java.util.List;

public class BoardReplyResponse {
    @Data
    public static class ParentItemDTO {
        private Integer replyId;
        private String nickname;
        private String profileImg;
        private String relativeTime;
        private String myTeamName;
        private String content;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;
        private List<ChildItemDTO> childReplies;

        public ParentItemDTO(Integer replyId, String nickname, String profileImg, String relativeTime, String myTeamName, String content, Boolean isOwner, Boolean isLike, Integer likeCount, List<ChildItemDTO> childReplies) {
            this.replyId = replyId;
            this.nickname = nickname;
            this.profileImg = profileImg;
            this.relativeTime = relativeTime;
            this.myTeamName = myTeamName;
            this.content = content;
            this.isOwner = isOwner;
            this.isLike = isLike;
            this.likeCount = likeCount;
            this.childReplies = childReplies;
        }
    }

    @Data
    public static class ChildItemDTO {
        private Integer replyId;
        private String nickname;
        private String profileImg;
        private String relativeTime;
        private String myTeamName;
        private String content;
        private Integer parentReplyId;
        private Integer tagReplyId;
        private String tagReplyName;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;

        public ChildItemDTO(Integer replyId, String nickname, String profileImg, String relativeTime, String myTeamName, String content, Integer parentReplyId, Integer tagReplyId, String tagReplyName, Boolean isOwner, Boolean isLike, Integer likeCount) {
            this.replyId = replyId;
            this.nickname = nickname;
            this.profileImg = profileImg;
            this.relativeTime = relativeTime;
            this.myTeamName = myTeamName;
            this.content = content;
            this.parentReplyId = parentReplyId;
            this.tagReplyId = tagReplyId;
            this.tagReplyName = tagReplyName;
            this.isOwner = isOwner;
            this.isLike = isLike;
            this.likeCount = likeCount;
        }
    }
}
