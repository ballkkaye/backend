package com.example.ballkkaye.game.today;

import com.example.ballkkaye._core.util.TodayGameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.ballkkaye._core.util.TodayGameUtil.round;

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
                    (String) row[5],  // homeTeamLogoUrl
                    (String) row[6],  // awayTeamLogoUrl
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

    // 날짜 기반 오늘 경기 리스트 조회
    public List<TodayGameResponse.ItemDTO> getTodayGames(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }
        List<Object[]> rows = todayGameRepository.findTodayGameList(today);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return rows.stream()
                .map(row -> {
                    Integer gameId = (Integer) row[0];
                    String gameStatus = (String) row[1];

                    String gameTime;
                    Object rawGameTime = row[2];
                    if (rawGameTime instanceof Timestamp ts) {
                        gameTime = ts.toLocalDateTime().format(formatter);
                    } else if (rawGameTime instanceof LocalDateTime ldt) {
                        gameTime = ldt.format(formatter);
                    } else {
                        gameTime = rawGameTime.toString(); // fallback
                    }

                    String stadiumName = TodayGameUtil.simplifyStadiumName((String) row[3]);

                    return new TodayGameResponse.ItemDTO(
                            gameId,
                            gameStatus,
                            gameTime,
                            stadiumName,
                            (String) row[4], // broadcastChannel
                            (String) row[5], // homePitcherName
                            (String) row[6], // homePitcherImg
                            (String) row[7], // awayPitcherName
                            (String) row[8], // awayPitcherImg
                            (String) row[9]  // ticketLink
                    );
                })
                .toList();
    }
}