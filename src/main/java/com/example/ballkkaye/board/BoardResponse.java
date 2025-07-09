package com.example.ballkkaye.board;

import com.example.ballkkaye.board.image.BoardImageResponse;
import com.example.ballkkaye.board.reply.BoardReplyResponse;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.team.TeamResponse;
import com.example.ballkkaye.user.User;
import lombok.Data;

import java.util.List;

public class BoardResponse {

    @Data
    public static class SaveDTO {
        private Integer boardId;
        private String title;
        private Integer teamId;
        private List<BoardImageResponse.ItemDTO> imagesUrl;
        private String content;
        private String nickname;
        private String relativeTime;
        private Integer likeCount;

        public SaveDTO(Board board, List<BoardImageResponse.ItemDTO> imagesUrl) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.teamId = board.getTeam().getId();
            this.content = board.getContent();
            this.imagesUrl = imagesUrl;
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer boardId;
        private String title;
        private Integer teamId;
        private List<BoardImageResponse.ItemDTO> imagesUrl;
        private String content;

        public UpdateDTO(Board board, List<BoardImageResponse.ItemDTO> imagesUrl) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.teamId = board.getTeam().getId();
            this.content = board.getContent();
            this.imagesUrl = imagesUrl;
        }
    }

    @Data
    public static class ItemDTO {
        private Integer boardId;
        private String title;
        private String nickname;
        private String relativeTime;
        private Integer teamId;
        private String teamName;
        private Integer likeCount;
        private Integer replyCount;

        public ItemDTO(Integer boardId, String title, String nickname, String relativeTime, Integer teamId, String teamName, Integer likeCount, Integer replyCount) {
            this.boardId = boardId;
            this.title = title;
            this.nickname = nickname;
            this.relativeTime = relativeTime;
            this.teamId = teamId;
            this.teamName = teamName;
            this.likeCount = likeCount;
            this.replyCount = replyCount;
        }
    }

    @Data
    public static class ListDTO {
        private List<TeamResponse.ItemDTO> teams;
        private List<ItemDTO> items;

        public ListDTO(List<TeamResponse.ItemDTO> teams, List<ItemDTO> items) {
            this.teams = teams;
            this.items = items;
        }
    }

    @Data
    public static class DetailWithReplyDTO {
        private Integer boardId;
        private String nickname;
        private String profileImageUrl;
        private String relativeTime;
        private String myTeamName;
        private Integer teamCategoryId;
        private String teamCategoryName;
        private String title;
        private String content;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;
        private List<BoardImageResponse.ItemDTO> images;
        private List<BoardReplyResponse.ParentItemDTO> replyItems;

        public DetailWithReplyDTO(Integer boardId, String nickname, String profileImageUrl, String relativeTime, String myTeamName, Integer teamCategoryId, String teamCategoryName, String title, String content, Boolean isOwner, Boolean isLike, Integer likeCount, List<BoardImageResponse.ItemDTO> images, List<BoardReplyResponse.ParentItemDTO> replyItems) {
            this.boardId = boardId;
            this.nickname = nickname;
            this.profileImageUrl = profileImageUrl;
            this.relativeTime = relativeTime;
            this.myTeamName = myTeamName;
            this.teamCategoryId = teamCategoryId;
            this.teamCategoryName = teamCategoryName;
            this.title = title;
            this.content = content;
            this.isOwner = isOwner;
            this.isLike = isLike;
            this.likeCount = likeCount;
            this.images = images;
            this.replyItems = replyItems;
        }
    }

    @Data
    public static class DeleteDTO {
        private String deleteStatus;

        public DeleteDTO() {
            this.deleteStatus = DeleteStatus.DELETED.getLabel();
        }
    }

    @Data
    public static class DetailDTO {
        private Integer boardId;
        private String nickname;
        private String profileImageUrl;
        private String relativeTime;
        private String myTeamName;
        private Integer teamCategoryId;
        private String teamCategoryName;
        private String title;
        private String content;
        private Boolean isOwner;
        private Boolean isLike;
        private Integer likeCount;
        private List<BoardImageResponse.ItemDTO> images;

        public DetailDTO(Board board, User user, String relativeTime, Boolean isOwner, Boolean isLike, Integer likeCount, List<BoardImageResponse.ItemDTO> images) {
            this.boardId = board.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileUrl();
            this.relativeTime = relativeTime;
            this.myTeamName = user.getTeam().getTeamName();
            this.teamCategoryId = board.getTeam().getId();
            this.teamCategoryName = board.getTeam().getTeamName();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isOwner = isOwner;
            this.isLike = isLike;
            this.likeCount = likeCount;
            this.images = images;
        }
    }
}
