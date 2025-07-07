package com.example.ballkkaye.stadium;

import lombok.Data;

public class StadiumResponse {

    @Data
    public static class ListDTO {
        private Integer stadiumId;
        private String stadiumName;


        public ListDTO(Stadium stadium) {
            this.stadiumId = stadium.getId();
            this.stadiumName = stadium.getStadiumName();

        }
    }
}
