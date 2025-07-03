package com.example.ballkkaye.game.today;

import lombok.Data;

public class TodayGameResponse {

    // 오늘 승리 예측 화면
    @Data
    public static class PredictionDTO {
        private Integer gameId;

        private String homeTeamName;
        private String awayTeamName;

        private String homePitcherName;
        private String awayPitcherName;

        private String homePitcherProfileUrl;
        private String awayPitcherProfileUrl;

        private Double homePredictionScore;
        private Double awayPredictionScore;
        private Double totalPredictionScore;

        private Double homeWinPercent;
        private Double awayWinPercent;

        public PredictionDTO(
                Integer gameId,
                String homeTeamName,
                String awayTeamName,
                String homePitcherName,
                String awayPitcherName,
                String homePitcherProfileUrl,
                String awayPitcherProfileUrl,
                Double homePredictionScore,
                Double awayPredictionScore,
                Double totalPredictionScore,
                Double homeWinPercent,
                Double awayWinPercent
        ) {
            this.gameId = gameId;
            this.homeTeamName = homeTeamName;
            this.awayTeamName = awayTeamName;
            this.homePitcherName = homePitcherName;
            this.awayPitcherName = awayPitcherName;
            this.homePitcherProfileUrl = homePitcherProfileUrl;
            this.awayPitcherProfileUrl = awayPitcherProfileUrl;
            this.homePredictionScore = homePredictionScore;
            this.awayPredictionScore = awayPredictionScore;
            this.totalPredictionScore = totalPredictionScore;
            this.homeWinPercent = homeWinPercent;
            this.awayWinPercent = awayWinPercent;
        }
    }

    @Data
    public static class ItemDTO {
        private Integer gameId;
        private String gameStatus;
        private String gameTime;
        private String stadiumName;
        private String broadcastChannel;
        private String homePitcherName;
        private String homePitcherImg;
        private String awayPitcherName;
        private String awayPitcherImg;
        private String ticketLink;

        public ItemDTO(Integer gameId, String gameStatus, String gameTime,
                       String stadiumName, String broadcastChannel,
                       String homePitcherName, String homePitcherImg,
                       String awayPitcherName, String awayPitcherImg,
                       String ticketLink) {
            this.gameId = gameId;
            this.gameStatus = gameStatus;
            this.gameTime = gameTime;
            this.stadiumName = stadiumName;
            this.broadcastChannel = broadcastChannel;
            this.homePitcherName = homePitcherName;
            this.homePitcherImg = homePitcherImg;
            this.awayPitcherName = awayPitcherName;
            this.awayPitcherImg = awayPitcherImg;
            this.ticketLink = ticketLink;
        }
    }

}
