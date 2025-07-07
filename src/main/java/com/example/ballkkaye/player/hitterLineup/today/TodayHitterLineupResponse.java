package com.example.ballkkaye.player.hitterLineup.today;

import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import lombok.Data;

import java.util.List;

public class TodayHitterLineupResponse {

    @Data
    public static class DTO {
        private Integer gameId;
        private String profileUrl;
        private String name;
        private Double ERA;
        private Integer gameCount;
        private String result;
        private Integer QS;
        private Double WHIP;
        private Integer season;

        private List<HitterInfo> hitters;


        @Data
        public static class HitterInfo {
            private Integer teamId;
            private Integer hitterOrder; // 타순
            private String name; // 선수명
            private String position; // 포지션 (내야수, 외야수, ...)
            private Integer ab; // 선발투수와 맞대결 전적: 타수
            private Integer h; // 선발투수와 맞대결 전적: 안타수
            private Double avg; // 선발투수와 맞대결 전적: 타울
            private Double ops; // 선발투수와 맞대결 전적: 출루율 + 장타율
            private Double hitPredictionPer; // 안타예측

            public HitterInfo(TodayHitterLineup todayHitterLineup, Double hitPredictionPer) {
                this.teamId = todayHitterLineup.getTeam().getId();
                this.hitterOrder = todayHitterLineup.getTodayHitterOrder();
                this.name = todayHitterLineup.getPlayer().getName();
                this.position = todayHitterLineup.getPosition();
                this.ab = todayHitterLineup.getAb();
                this.h = todayHitterLineup.getH();
                this.avg = todayHitterLineup.getAvg();
                this.ops = todayHitterLineup.getOps();
                this.hitPredictionPer = hitPredictionPer;
            }
        }


        public DTO(Integer gameId, TodayStartingPitcher todayStartingPitcher, Integer season, List<HitterInfo> hitters) {
            this.gameId = gameId;
            this.profileUrl = todayStartingPitcher.getProfileUrl();
            this.name = todayStartingPitcher.getPlayer().getName();
            this.ERA = todayStartingPitcher.getERA();
            this.gameCount = todayStartingPitcher.getGameCount();
            this.result = todayStartingPitcher.getResult();
            this.QS = todayStartingPitcher.getQS();
            this.WHIP = todayStartingPitcher.getWHIP();
            this.season = season;
            this.hitters = hitters;
        }
    }
}
