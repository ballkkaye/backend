package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye._core.util.Base64Util;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImage;
import lombok.Data;

import java.time.format.DateTimeFormatter;

public class VisitRecordResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String homeTeamName;
        private String awayTeamName;
        private Integer homeScore;
        private Integer awayScore;
        private String gameDate;      // "2025.07.03"
        private String stadiumName;
        private String result;
        private String DeleteStatus;
        private String content;
        private String imageString; // base64로 변환된 문자열

        public DTO(VisitRecord visitRecord, VisitRecordImage image) {
            this.id = visitRecord.getId();
            this.homeTeamName = visitRecord.getGame().getHomeTeam().getTeamName().split(" ")[0];
            this.awayTeamName = visitRecord.getGame().getAwayTeam().getTeamName().split(" ")[0];
            this.homeScore = visitRecord.getGame().getHomeResultScore();
            this.awayScore = visitRecord.getGame().getAwayResultScore();
            this.gameDate = visitRecord.getGame().getGameTime()
                    .toLocalDateTime()
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.stadiumName = visitRecord.getGame().getStadium().getStadiumName();
            this.result = visitRecord.getResult().getValue(); // "승", "패", "무"
            this.DeleteStatus = visitRecord.getDeleteStatus().getLabel(); // "정상", "삭제됨"
            this.content = visitRecord.getContent();
            if (image != null) {
                String imageUrl = image.getImageUrl();
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                byte[] bytes = Base64Util.readImageAsByteArray("visit-record/" + filename);
                String mimeType;
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) mimeType = "image/jpeg";
                else if (filename.endsWith(".png")) mimeType = "image/png";
                else if (filename.endsWith(".gif")) mimeType = "image/gif";
                else throw new IllegalArgumentException("지원하지 않는 이미지 확장자입니다: " + filename);

                this.imageString = Base64Util.encodeAsString(bytes, mimeType);
            } else {
                this.imageString = null;
            }
        }

    }

    @Data
    public static class ListDTO {
        private Integer id;
        private String homeTeamName;
        private String awayTeamName;
        private Integer homeScore;
        private Integer awayScore;
        private String gameDate;      // "2025.07.03"
        private String stadiumName;

        public ListDTO(VisitRecord visitRecord) {
            this.id = visitRecord.getId();
            this.homeTeamName = visitRecord.getGame().getHomeTeam().getTeamName().split(" ")[0];
            this.awayTeamName = visitRecord.getGame().getAwayTeam().getTeamName().split(" ")[0];
            this.homeScore = visitRecord.getGame().getHomeResultScore();
            this.awayScore = visitRecord.getGame().getAwayResultScore();
            this.gameDate = visitRecord.getGame().getGameTime()
                    .toLocalDateTime()
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.stadiumName = visitRecord.getGame().getStadium().getStadiumName();
        }
    }


    @Data
    public static class DetailDTO {
        private Integer id;
        private String homeTeamName;
        private String awayTeamName;
        private Integer homeScore;
        private Integer awayScore;
        private String gameDate;      // "2025.07.03"
        private String stadiumName;
        private String result;
        private String content;
        private String imageString; // base64로 변환된 문자열

        public DetailDTO(VisitRecord visitRecord, VisitRecordImage image) {
            this.id = visitRecord.getId();
            this.homeScore = visitRecord.getGame().getHomeResultScore();
            this.awayScore = visitRecord.getGame().getAwayResultScore();
            this.gameDate = visitRecord.getGame().getGameTime()
                    .toLocalDateTime()
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.homeTeamName = visitRecord.getGame().getHomeTeam().getTeamName().split(" ")[0];
            this.awayTeamName = visitRecord.getGame().getAwayTeam().getTeamName().split(" ")[0];
            this.stadiumName = visitRecord.getGame().getStadium().getStadiumName();
            this.result = visitRecord.getResult().getValue(); // "승", "패", "무"
            this.content = visitRecord.getContent();
            if (image != null) {
                String imageUrl = image.getImageUrl();
                String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                byte[] bytes = Base64Util.readImageAsByteArray("visit-record/" + filename);
                String mimeType;
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) mimeType = "image/jpeg";
                else if (filename.endsWith(".png")) mimeType = "image/png";
                else if (filename.endsWith(".gif")) mimeType = "image/gif";
                else throw new IllegalArgumentException("지원하지 않는 이미지 확장자입니다: " + filename);

                this.imageString = Base64Util.encodeAsString(bytes, mimeType);
            } else {
                this.imageString = null;
            }
        }
    }
}
