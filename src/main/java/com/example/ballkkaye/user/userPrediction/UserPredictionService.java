package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.common.enums.PredictionStatus;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPredictionService {

    private final UserPredictionRepository userPredictionRepository;
    private final UserRepository userRepository;

    public List<UserPredictionResponse.TodayGameDTO> getTodayGames(Integer userId) {
        LocalDate today = LocalDate.now();
        List<UserPredictionResponse.TodayGameDTO> rawList =
                userPredictionRepository.findTodayGamesForPrediction(today, userId);

        List<UserPredictionResponse.TodayGameDTO> finalList = new ArrayList<>();

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

            finalList.add(raw);
        }

        return finalList;
    }

    public List<UserPredictionResponse.MyPredictionDTO> findMyPredictions(Integer userId, LocalDate date) {
        List<UserPredictionResponse.MyPredictionDTO> myPredictions =
                userPredictionRepository.findMyPredictions(userId, date);

        for (UserPredictionResponse.MyPredictionDTO prediction : myPredictions) {
            GameStatus gameStatus = prediction.getGameStatus();
            Integer userChoiceTeamId = prediction.getUserChoiceTeamId();
            Integer homeScore = prediction.getHomeScore();
            Integer awayScore = prediction.getAwayScore();
            Integer homeTeamId = prediction.getHomeTeam().getTeamId();
            Integer awayTeamId = prediction.getAwayTeam().getTeamId();
            LocalDateTime updatedAt = prediction.getUpdatedAt();

            PredictionStatus predictionStatus;

            // 오늘의 게임 결과가 갱신되지 않았으면 예측 결과는 WAITING
            if (updatedAt == null) {
                predictionStatus = PredictionStatus.WAITING;
            }
            // 경기 상태 기반으로 결과 처리
            else {
                switch (gameStatus) {
                    case SCHEDULED:
                    case DELAYED:
                    case IN_PROGRESS:
                        predictionStatus = PredictionStatus.WAITING;
                        break;

                    case COMPLETED:
                        if (homeScore == null || awayScore == null) {
                            predictionStatus = PredictionStatus.TIE; // 예외 상황: score 없으면 취소로 간주
                        } else if (homeScore > awayScore) {
                            predictionStatus = userChoiceTeamId != null && userChoiceTeamId.equals(homeTeamId)
                                    ? PredictionStatus.CORRECT
                                    : PredictionStatus.INCORRECT;
                        } else if (awayScore > homeScore) {
                            predictionStatus = userChoiceTeamId != null && userChoiceTeamId.equals(awayTeamId)
                                    ? PredictionStatus.CORRECT
                                    : PredictionStatus.INCORRECT;
                        } else {
                            predictionStatus = PredictionStatus.TIE; // 무승부
                        }
                        break;

                    case CANCELLED:
                        predictionStatus = PredictionStatus.TIE;
                        break;

                    default:
                        predictionStatus = null;
                }
            }

            prediction.setPredictionStatus(predictionStatus);
        }

        return myPredictions;
    }


    @Transactional
    public List<UserPredictionRequest.SaveDTO> save(Integer userId, List<UserPredictionRequest.SaveDTO> saveDTO) {
        // 1. 유저가 존재하지 않으면 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. Repository에 user 객체에서 ID만 넘기기
        userPredictionRepository.saveAll(user.getId(), saveDTO);

        // 3. 요청한 값 그대로 응답
        return saveDTO;
    }

}