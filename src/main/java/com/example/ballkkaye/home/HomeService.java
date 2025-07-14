package com.example.ballkkaye.home;


import com.example.ballkkaye._core.util.TodayGameUtil;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.game.today.TodayGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                todayGameRepository.findTodayGameList(today).stream()
                        .map(game -> new HomeResponse.DTO.TodayGameDTO(
                                game.getGameId(),
                                GameStatus.valueOf(game.getGameStatus()),
                                game.getGameTime(),
                                TodayGameUtil.simplifyStadiumName(game.getStadiumName()),
                                game.getBroadcastChannel(),
                                game.getHomePitcherName(),
                                game.getHomeTeamLogoUrl(),
                                game.getAwayPitcherName(),
                                game.getAwayTeamLogoUrl(),
                                game.getTicketLink()
                        )).toList();

        // 2. 승리 예측
        List<HomeResponse.DTO.WinPredictionDTO> predictions =
                todayGameRepository.findTodayGamePredictionData().stream()
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