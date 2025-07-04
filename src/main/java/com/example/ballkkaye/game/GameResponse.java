package com.example.ballkkaye.game;

import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GameResponse {

    @Data
    public static class ListDTO {
        private String selectedDate;
        private List<GroupedByDateDTO> games;

        public ListDTO(String date, List<GroupedByDateDTO> games) {
            this.selectedDate = date;
            this.games = games;
        }
    }

    @Data
    public static class GroupedByDateDTO {
        private String gameDate;
        private List<ItemDTO> items;

        public GroupedByDateDTO(String gameDate, List<ItemDTO> items) {
            this.gameDate = gameDate;
            this.items = items;
        }

        @Data
        public static class ItemDTO {
            private Integer gameId;
            private String homeTeamName;
            private Integer homeTeamScore;
            private String awayTeamName;
            private Integer awayTeamScore;
            private String stadiumFullName;
            private String stadiumShortName;
            private String gameDate;

            public ItemDTO(Game game) {
                this.gameId = game.getId();
                this.homeTeamName = game.getHomeTeam().getTeamName();
                this.homeTeamScore = game.getHomeResultScore();
                this.awayTeamName = game.getAwayTeam().getTeamName();
                this.awayTeamScore = game.getAwayResultScore();
                this.stadiumFullName = game.getStadium().getStadiumName();
                this.stadiumShortName = game.getStadium().getStadiumName().substring(0, 2);
                this.gameDate = game.getGameTime().toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
    }
}
