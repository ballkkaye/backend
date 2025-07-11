package com.example.ballkkaye.home;

import lombok.Data;

import java.util.List;

public class HomeResponse {

    @Data
    public static class DTO {
        private List<TodayGameDTO> todayGames;
        private List<WinPredictionDTO> winPredictions;
        private List<BoardDTO> boards;


        // 오늘의 경기
        @Data
        public static class TodayGameDTO {
            private Integer gameId;
            private String gameStatus;
            private String gameTime;
            private String stadiumName;
            private String broadcastChannel;
            private String homePitcherName;
            private String homeTeamLogoUrl;
            private String awayPitcherName;
            private String awayTeamLogoUrl;
            private String ticketLink;

            public TodayGameDTO(Integer gameId, String gameStatus, String gameTime,
                                String stadiumName, String broadcastChannel,
                                String homePitcherName, String homeTeamLogoUrl,
                                String awayPitcherName, String awayTeamLogoUrl,
                                String ticketLink) {
                this.gameId = gameId;
                this.gameStatus = gameStatus;
                this.gameTime = gameTime;
                this.stadiumName = stadiumName;
                this.broadcastChannel = broadcastChannel;
                this.homePitcherName = homePitcherName;
                this.homeTeamLogoUrl = homeTeamLogoUrl;
                this.awayPitcherName = awayPitcherName;
                this.awayTeamLogoUrl = awayTeamLogoUrl;
                this.ticketLink = ticketLink;
            }
        }

        // 승리 예측
        @Data
        public static class WinPredictionDTO {
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

            public WinPredictionDTO(
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

        // 커뮤니티
        @Data
        public static class BoardDTO {
            private String title;
            private String content;


            public BoardDTO(String title, String content) {
                this.title = title;
                this.content = content;
            }

        }

        public DTO(List<TodayGameDTO> todayGames, List<WinPredictionDTO> winPredictions, List<BoardDTO> boards) {
            this.todayGames = todayGames;
            this.winPredictions = winPredictions;
            this.boards = boards;
        }
    }

}
