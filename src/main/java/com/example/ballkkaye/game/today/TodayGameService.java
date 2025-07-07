package com.example.ballkkaye.game.today;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodayGameService {
    private final TodayGameRepository todayGameRepository;

    // 유저 예측 결과 조회용
    public List<TodayGameResponse.PredictionDTO> getTodayGamePredictions() {
        List<Object[]> results = todayGameRepository.findTodayGamePredictionData();

        List<TodayGameResponse.PredictionDTO> PredictionDTO = new ArrayList<>();
        for (Object[] row : results) {
            TodayGameResponse.PredictionDTO dto = new TodayGameResponse.PredictionDTO(
                    (Integer) row[0], // gameId
                    (String) row[1],  // homeTeamName
                    (String) row[2],  // awayTeamName
                    (String) row[3],  // homePitcherName
                    (String) row[4],  // awayPitcherName
                    (String) row[5],  // homePitcherProfileUrl
                    (String) row[6],  // awayPitcherProfileUrl
                    round((Double) row[7]), // homePredictionScore
                    round((Double) row[8]), // awayPredictionScore
                    round((Double) row[9]), // totalPredictionScore
                    round((Double) row[10]), // homeWinPercent
                    round((Double) row[11])  // awayWinPercent
            );
            PredictionDTO.add(dto);
        }
        return PredictionDTO;
    }

    private double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10) / 10.0;
    }

    // 날짜 기반 오늘 경기 리스트 조회
    public List<TodayGameResponse.ItemDTO> getTodayGames(LocalDate date) {
        return todayGameRepository.findTodayGameList(date);
    }
}