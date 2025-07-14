package com.example.ballkkaye.player.hitterLineup.today;

import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye._core.util.HitterLineupUtil;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcher;
import com.example.ballkkaye.player.startingPitcher.today.TodayStartingPitcherRepository;
import com.example.ballkkaye.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TodayHitterLineupService {
    private final TodayHitterLineupRepository todayHitterLineUpRepository;
    private final GameRepository gameRepository;
    private final TodayStartingPitcherRepository todayStartingPitcherRepository;


    public TodayHitterLineupResponse.DTO getDetailMatchup(Integer gameId, Integer teamId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ExceptionApi404("경기 정보가 없습니다"));

        // 선택한 팀 기준으로 타자 팀/상대 투수 팀 결정
        Team hitterTeam;
        Team pitcherTeam;
        if (teamId.equals(game.getHomeTeam().getId())) {
            hitterTeam = game.getHomeTeam();
            pitcherTeam = game.getAwayTeam();
        } else {
            hitterTeam = game.getAwayTeam();
            pitcherTeam = game.getHomeTeam();
        }

        // 선발투수 정보 조회 (상대 팀 기준, Today 기준)
        TodayStartingPitcher todayPitcher = todayStartingPitcherRepository
                .findByGameIdAndTeam(gameId, pitcherTeam.getTeamName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ExceptionApi404("선발투수 정보가 없습니다."));

        // 선택한 팀 기준으로 타자 라인업 조회 (Today 기준)
        List<TodayHitterLineup> hitterLineups = todayHitterLineUpRepository.findByGameIdAndTeamId(gameId, hitterTeam.getId());

        if (hitterLineups.isEmpty()) {
            throw new ExceptionApi404("타자 라인업 정보가 없습니다.");
        }

        // HitterInfo 리스트 변환
        List<TodayHitterLineupResponse.DTO.HitterInfo> hitterDTOs = hitterLineups.stream()
                .map(h -> {
                    // 시즌 타율은 계산용으로만 사용
                    Double prediction = HitterLineupUtil.calculateHitProbability(
                            h.getSeasonAvg(), h.getAb(), h.getAvg()
                    );

                    return new TodayHitterLineupResponse.DTO.HitterInfo(h, prediction);
                })
                .toList();

        // 시즌 정보는 gameTime의 연도 기준
        Integer season = game.getGameTime().toLocalDateTime().getYear();

        // 최종 응답 DTO 구성
        return new TodayHitterLineupResponse.DTO(
                gameId,
                todayPitcher,
                season,
                hitterDTOs
        );
    }
}
