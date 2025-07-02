package com.example.ballkkaye.board.reply;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.user.User;
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

    @Data
    public static class SaveDTO {
        private Integer replyId;
        private Integer boardId;
        private Integer userId;
        private String nickname;
        private String profileImg;
        private String teamName;
        private Integer parentReplyId;
        private Integer tagReplyId;
        private String tagReplyName;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;
        private String content;
        private String relativeTime;

        public SaveDTO(BoardReply boardReply, Board boardPS, User user, String relativeTime) {
            this.replyId = boardReply.getId();
            this.boardId = boardPS.getId();
            this.userId = user.getId();
            this.nickname = user.getNickname();
            this.profileImg = user.getProfileUrl();
            this.teamName = user.getTeam() == null ? null : user.getTeam().getTeamName();
            this.parentReplyId = boardReply.getParentReplyId() == null ? null : boardReply.getParentReplyId().getId();
            this.tagReplyId = boardReply.getTagReplyId() == null ? null : boardReply.getTagReplyId().getId();
            this.tagReplyName = boardReply.getTagReplyId() == null ? null : boardReply.getTagReplyId().getUser().getNickname();
            this.isOwner = true;
            this.isLike = false;
            this.likeCount = 0;
            this.content = boardReply.getContent();
            this.relativeTime = relativeTime;
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer replyId;
        private Integer boardId;
        private Integer userId;
        private String nickname;
        private String profileImg;
        private String teamName;
        private Integer parentReplyId;
        private Integer tagReplyId;
        private String tagReplyName;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;
        private String content;
        private String relativeTime;

        public UpdateDTO(BoardReply boardReply, String relativeTime, Integer replyLikeCount, Boolean isLike) {
            this.replyId = boardReply.getId();
            this.boardId = boardReply.getBoard().getId();
            this.userId = boardReply.getUser().getId();
            this.nickname = boardReply.getUser().getNickname();
            this.profileImg = boardReply.getUser().getProfileUrl();
            this.teamName = boardReply.getUser().getTeam() == null ? null : boardReply.getUser().getTeam().getTeamName();
            this.parentReplyId = boardReply.getParentReplyId() == null ? null : boardReply.getParentReplyId().getId();
            this.tagReplyId = boardReply.getTagReplyId() == null ? null : boardReply.getTagReplyId().getId();
            this.tagReplyName = boardReply.getTagReplyId() == null ? null : boardReply.getTagReplyId().getUser().getNickname();
            this.isOwner = true;
            this.isLike = isLike;
            this.likeCount = replyLikeCount;
            this.content = boardReply.getContent();
            this.relativeTime = relativeTime;
        }
    }
}
