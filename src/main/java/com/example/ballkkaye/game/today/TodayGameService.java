package com.example.ballkkaye.game.today;

import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodayGameService {
    private final TodayGameRepository todayGameRepository;
    private final TodayStartingPitcherRepository todayStartingPitcherRepository;

    // 유저 예측 결과 조회용
    public List<TodayGameResponse.PredictionDTO> getTodayGamePredictions() {
        List<TodayGame> games = todayGameRepository.findTodayGames();
        List<TodayGameResponse.PredictionDTO> respDTO = new ArrayList<>();

        for (TodayGame game : games) {

            TodayStartingPitcher homePitcher = null;
            TodayStartingPitcher awayPitcher = null;

            List<TodayStartingPitcher> startingPitchers = todayStartingPitcherRepository.findByGameId(game.getGame().getId());
            for (TodayStartingPitcher startingPitcher : startingPitchers) {
                if (startingPitcher.getPlayer().getTeam().getId().equals(game.getHomeTeam().getId())) {
                    homePitcher = startingPitcher;
                } else if (startingPitcher.getPlayer().getTeam().getId().equals(game.getAwayTeam().getId())) {
                    awayPitcher = startingPitcher;
                }
            }
            TodayGameResponse.PredictionDTO dto = new TodayGameResponse.PredictionDTO(
                    game.getGame().getId(),
                    game.getHomeTeam().getId(),
                    game.getAwayTeam().getId(),
                    game.getHomeTeam().getTeamName(),
                    game.getAwayTeam().getTeamName(),
                    homePitcher.getPlayer().getName(),
                    awayPitcher.getPlayer().getName(),
                    homePitcher.getProfileUrl(),
                    awayPitcher.getProfileUrl(),
                    game.getHomePredictionScore(),
                    game.getAwayPredictionScore(),
                    game.getTotalPredictionScore(),
                    game.getHomeWinPer(),
                    game.getAwayWinPer()
            );
            respDTO.add(dto);
        }
        return respDTO;
    }

    // 날짜 기반 오늘 경기 리스트 조회
    public List<TodayGameResponse.ItemDTO> getTodayGames(LocalDate date) {
        LocalDate selectDate = date == null ? LocalDate.now() : date;
        return todayGameRepository.findTodayGameList(selectDate);
    }
}