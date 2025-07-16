package com.example.ballkkaye.home;


import com.example.ballkkaye._core.util.TodayGameUtil;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
    private final TodayGameRepository todayGameRepository;
    private final BoardRepository boardRepository;
    private final TodayStartingPitcherRepository todayStartingPitcherRepository;


    public HomeResponse.DTO getHome() {
        LocalDate today = LocalDate.now();
        log.info("홈 화면 데이터 조회 시작 - 날짜: {}", today);

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
        log.info("오늘 경기 수: {}", todayGames.size());

        // 2. 승리 예측
        List<TodayGame> games = todayGameRepository.findTodayGames();
        log.info("예측 대상 경기 수: {}", games.size());
        List<HomeResponse.DTO.WinPredictionDTO> predictions = new ArrayList<>();

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
            HomeResponse.DTO.WinPredictionDTO dto = new HomeResponse.DTO.WinPredictionDTO(
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
            predictions.add(dto);
        }


        // 3. 커뮤니티 최신글 5개
        List<HomeResponse.DTO.BoardDTO> boards =
                boardRepository.findLatest5().stream()
                        .map(b -> new HomeResponse.DTO.BoardDTO(
                                b.getTitle(),
                                b.getContent()
                        )).toList();
        log.info("최신 커뮤니티 글 수: {}", boards.size());

        return new HomeResponse.DTO(todayGames, predictions, boards);
    }
}