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

    // 승리 예측 1 화면
    public List<TodayGameResponse.PredictionDTO> getTodayGamePredictions(LocalDate today) {
        // 1. 오늘 날짜 기준으로 게임 데이터 Object[] 형태로 조회
        List<Object[]> rows = todayGameRepository.findTodayGameList(today);

        List<TodayGameResponse.PredictionDTO> predictions = new ArrayList<>();

        for (Object[] row : rows) {
            Integer gameId = (Integer) row[0];                      // 게임 ID
            String homeTeamName = TodayGameUtil.simplifyTeamName((String) row[1]);  // 홈팀명 (단순화)
            String awayTeamName = TodayGameUtil.simplifyTeamName((String) row[2]);  // 어웨이팀명 (단순화)
            String homePitcherName = (String) row[3];              // 홈 선발투수 이름
            String awayPitcherName = (String) row[4];              // 어웨이 선발투수 이름
            String homePitcherProfileUrl = (String) row[5];        // 홈 투수 프로필 이미지
            String awayPitcherProfileUrl = (String) row[6];        // 어웨이 투수 프로필 이미지
            Double homePredictionScore = round((Double) row[7]);   // 홈 예측 점수
            Double awayPredictionScore = round((Double) row[8]);   // 어웨이 예측 점수
            Double totalPredictionScore = round((Double) row[9]);  // 총 예측 점수
            Double homeWinPercent = round((Double) row[10]);       // 홈팀 승률
            Double awayWinPercent = round((Double) row[11]);       // 어웨이팀 승률

            predictions.add(new TodayGameResponse.PredictionDTO(
                    gameId,
                    homeTeamName,
                    awayTeamName,
                    homePitcherName,
                    awayPitcherName,
                    homePitcherProfileUrl,
                    awayPitcherProfileUrl,
                    homePredictionScore,
                    awayPredictionScore,
                    totalPredictionScore,
                    homeWinPercent,
                    awayWinPercent
            ));
        }

        return predictions;
    }

    // 날짜 기반 오늘 경기 리스트 조회
    public List<TodayGameResponse.ItemDTO> getTodayGames(LocalDate today) {
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
