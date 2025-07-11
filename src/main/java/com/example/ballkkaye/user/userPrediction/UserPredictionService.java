package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.common.enums.PredictionStatus;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserPredictionService {

    private final UserPredictionRepository userPredictionRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public List<UserPredictionResponse.TodayGameDTO> todayGames(Integer userId) {
        LocalDate today = LocalDate.now();
        List<UserPredictionResponse.TodayGameDTO> rawList =
                userPredictionRepository.findTodayGamesForPrediction(today, userId);

        List<UserPredictionResponse.TodayGameDTO> TodayGameDTO = new ArrayList<>();

        for (UserPredictionResponse.TodayGameDTO raw : rawList) {
            int homeCount = raw.getHomeVoteRate();
            int awayCount = raw.getAwayVoteRate();

            int total = homeCount + awayCount;

            // 투표 수 없으면 기본값 0%, 있으면 비율 계산
            int homeRate = 0;
            int awayRate = 0;

            if (total > 0) {
                homeRate = Math.round((homeCount * 100f) / total);
                awayRate = 100 - homeRate;
            }

            raw.setHomeVoteRate(homeRate);
            raw.setAwayVoteRate(awayRate);

            TodayGameDTO.add(raw);
        }

        return TodayGameDTO;
    }

    public List<UserPredictionResponse.MyPredictionDTO> findMyPredictions(Integer userId, LocalDate date) {
        List<UserPredictionResponse.MyPredictionDTO> MyPredictionDTO =
                userPredictionRepository.findMyPredictions(userId, date);

        for (UserPredictionResponse.MyPredictionDTO prediction : MyPredictionDTO) {
            GameStatus gameStatus = prediction.getGameStatus();
            Integer userChoiceTeamId = prediction.getUserChoiceTeamId();
            Integer homeScore = prediction.getHomeScore();
            Integer awayScore = prediction.getAwayScore();
            Integer homeTeamId = prediction.getHomeTeam().getTeamId();
            Integer awayTeamId = prediction.getAwayTeam().getTeamId();

            PredictionStatus predictionStatus;

            switch (gameStatus) {
                case SCHEDULED:
                case DELAYED:
                case IN_PROGRESS:
                    predictionStatus = PredictionStatus.WAITING;
                    break;

                case COMPLETED:
                    if (homeScore == null || awayScore == null) {
                        predictionStatus = PredictionStatus.TIE;
                    } else if (homeScore > awayScore) {
                        predictionStatus = userChoiceTeamId != null && userChoiceTeamId.equals(homeTeamId)
                                ? PredictionStatus.CORRECT
                                : PredictionStatus.INCORRECT;
                    } else if (awayScore > homeScore) {
                        predictionStatus = userChoiceTeamId != null && userChoiceTeamId.equals(awayTeamId)
                                ? PredictionStatus.CORRECT
                                : PredictionStatus.INCORRECT;
                    } else {
                        predictionStatus = PredictionStatus.TIE;
                    }
                    break;

                case CANCELLED:
                    predictionStatus = PredictionStatus.TIE;
                    break;

                default:
                    predictionStatus = null;
            }

            prediction.setPredictionStatus(predictionStatus);
        }

        return MyPredictionDTO;
    }

    @Transactional
    public List<UserPredictionRequest.SaveDTO> save(Integer userId, List<UserPredictionRequest.SaveDTO> saveDTOs) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. User ID: " + userId));

        LocalDateTime now = LocalDateTime.now();
        Set<Integer> gameIdsInCurrentRequest = new HashSet<>();
        List<UserPrediction> predictionsToSave = new ArrayList<>();

        for (UserPredictionRequest.SaveDTO dto : saveDTOs) {
            Integer gameId = dto.getGameId();
            Integer teamId = dto.getUserChoiceTeamId();

            if (gameId == null) {
                throw new IllegalArgumentException("예측할 경기 ID가 누락되었습니다.");
            }
            if (teamId == null) {
                throw new IllegalArgumentException("경기 [ID: " + gameId + "]에 대한 선택 팀 정보가 누락되었습니다.");
            }
            if (!gameIdsInCurrentRequest.add(gameId)) {
                throw new IllegalArgumentException("요청 내에 경기 [ID: " + gameId + "]에 대한 중복 예측 요청이 있습니다.");
            }

            if (userPredictionRepository.isExistsByUserIdAndGameId(userId, gameId)) {
                throw new IllegalArgumentException("경기 [ID: " + gameId + "]는 이미 예측되었습니다.");
            }

            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 경기 ID입니다: " + gameId));

            if (game.getGameTime().toLocalDateTime().isBefore(now)) {
                throw new IllegalArgumentException("경기 [ID: " + gameId + "]는 이미 시작되었거나 종료되었습니다.");
            }

            if (!teamId.equals(game.getHomeTeam().getId()) && !teamId.equals(game.getAwayTeam().getId())) {
                throw new IllegalArgumentException("선택 팀 [ID: " + teamId + "]은 해당 경기의 팀이 아닙니다.");
            }

            if (game.getGameStatus() != GameStatus.SCHEDULED) {
                throw new IllegalArgumentException("경기 [ID: " + gameId + "]는 예측 가능한 상태가 아닙니다.");
            }

            Team chosenTeam = teamId.equals(game.getHomeTeam().getId()) ? game.getHomeTeam() : game.getAwayTeam();

            predictionsToSave.add(UserPrediction.builder()
                    .user(user)
                    .game(game)
                    .userChoiceTeam(chosenTeam)
                    .result(PredictionStatus.WAITING)
                    .build());
        }

        if (!predictionsToSave.isEmpty()) {
            userPredictionRepository.saveAll(predictionsToSave);
        }
        return saveDTOs;
    }
}