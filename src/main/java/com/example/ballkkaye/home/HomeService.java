package com.example.ballkkaye.home;


import com.example.ballkkaye._core.util.TodayGameUtil;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.game.today.TodayGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.ballkkaye._core.util.TodayGameUtil.round;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final TodayGameRepository todayGameRepository;
    private final BoardRepository boardRepository;


    public HomeResponse.DTO getHome() {
        LocalDate today = LocalDate.now();

        // 1. 오늘의 경기
        List<HomeResponse.DTO.TodayGameDTO> todayGames =
                // 1. 오늘 날짜 기준으로 게임 데이터 Object[] 형태로 조회
                todayGameRepository.findTodayGameList(today).stream()
                        .map(row -> {
                            // [0] 경기 ID
                            Integer gameId = (Integer) row[0];
                            // [1] 경기 상태 (예: 예정, 진행중, 종료 등)
                            String gameStatus = (String) row[1];

                            // [2] 경기 시작 시간 (HH:mm 형식으로 포맷팅)
                            String gameTime;
                            Object rawGameTime = row[2];
                            if (rawGameTime instanceof Timestamp ts) {
                                gameTime = ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                            } else if (rawGameTime instanceof LocalDateTime ldt) {
                                gameTime = ldt.format(DateTimeFormatter.ofPattern("HH:mm"));
                            } else {
                                gameTime = rawGameTime.toString(); // fallback (예외 케이스)
                            }

                            // [3] 구장 이름 → 단순화 함수 적용 (예: "인천SSG랜더스필드" → "인천")
                            String rawStadiumName = (String) row[3];
                            String stadiumName = TodayGameUtil.simplifyStadiumName(rawStadiumName);

                            // [4~9] 기타 정보 → 그대로 DTO로 매핑
                            return new HomeResponse.DTO.TodayGameDTO(
                                    gameId,
                                    gameStatus,
                                    gameTime,
                                    stadiumName,
                                    (String) row[4],  // broadcastChannel
                                    (String) row[5],  // homePitcherName
                                    (String) row[6],  // homeTeamLogoUrl
                                    (String) row[7],  // awayPitcherName
                                    (String) row[8],  // awayTeamLogoUrl
                                    (String) row[9]   // ticketLink
                            );
                        })
                        .toList();

        // 2. 승리 예측
        List<HomeResponse.DTO.WinPredictionDTO> predictions =
                todayGameRepository.findTodayGamePredictionData(today).stream()
                        .map(row -> new HomeResponse.DTO.WinPredictionDTO(
                                (Integer) row[0],  // gameId
                                TodayGameUtil.simplifyTeamName((String) row[1]),  // homeTeamName → 앞 단어만
                                TodayGameUtil.simplifyTeamName((String) row[2]),   // awayTeamName → 앞 단어만
                                (String) row[3],   // homePitcherName
                                (String) row[4],   // awayPitcherName
                                (String) row[5],   // homePitcherProfileUrl
                                (String) row[6],   // awayPitcherProfileUrl
                                round((Double) row[7]),
                                round((Double) row[8]),
                                round((Double) row[9]),
                                round((Double) row[10]),
                                round((Double) row[11])
                        )).toList();

        // 3. 커뮤니티 최신글 5개
        List<HomeResponse.DTO.BoardDTO> boards =
                boardRepository.findLatest5().stream()
                        .map(b -> new HomeResponse.DTO.BoardDTO(
                                b.getTitle(),
                                b.getContent()
                        )).toList();

        return new HomeResponse.DTO(todayGames, predictions, boards);
    }


}
