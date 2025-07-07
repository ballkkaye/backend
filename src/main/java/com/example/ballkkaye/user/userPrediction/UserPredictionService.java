package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.common.enums.PredictionStatus;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.team.Team;
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
    private final TodayGameRepository todayGameRepository;

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
            LocalDateTime updatedAt = prediction.getUpdatedAt();

            PredictionStatus predictionStatus;

            if (updatedAt == null) {
                predictionStatus = PredictionStatus.WAITING;
            } else {
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
            }

            prediction.setPredictionStatus(predictionStatus);
        }

        return MyPredictionDTO;
    }


    @Transactional
    public List<UserPredictionRequest.SaveDTO> save(Integer userId, List<UserPredictionRequest.SaveDTO> saveDTO) {
        // 1. 유저 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. User ID: " + userId));

        // 저장할 UserPrediction 엔티티들을 담을 리스트
        List<UserPrediction> userPredictionsToSave = new ArrayList<>();

        // 예측 요청 목록을 순회하며 각 예측에 대한 유효성 검사 및 UserPrediction 엔티티 생성
        for (UserPredictionRequest.SaveDTO dto : saveDTO) {
            // 2. 유효성 검사
            if (dto.getGameId() == null) {
                throw new IllegalArgumentException("예측할 경기 ID가 누락되었습니다.");
            }
            if (dto.getUserChoiceTeamId() == null) {
                throw new IllegalArgumentException("경기 [ID: " + dto.getGameId() + "]에 대한 선택 팀 정보가 누락되었습니다.");
            }

            // 3. 이미 예측한 경기가 있으면 예외 처리
            if (userPredictionRepository.isExistsByUserIdAndGameId(user.getId(), dto.getGameId())) {
                throw new IllegalArgumentException("경기 [ID: " + dto.getGameId() + "]는 이미 예측되었습니다. 중복 예측은 불가능합니다.");
            }

            // 4. TodayGame 엔티티 조회
            TodayGame todayGame = todayGameRepository.findByGameId(dto.getGameId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 경기 ID입니다: " + dto.getGameId()));

            // 5. 선택한 팀이 해당 경기의 홈/원정 팀에 속하는지 확인
            if (!dto.getUserChoiceTeamId().equals(todayGame.getHomeTeam().getId()) &&
                    !dto.getUserChoiceTeamId().equals(todayGame.getAwayTeam().getId())) {
                throw new IllegalArgumentException("경기 [ID: " + dto.getGameId() + "]에 대한 선택 팀 [ID: " + dto.getUserChoiceTeamId() + "]이 유효하지 않습니다. 해당 경기의 홈/원정 팀 중에서 선택해야 합니다.");
            }

            // 6. 경기가 예측 가능한 상태인지 확인 (SCHEDULED만 허용)
            if (todayGame.getGameStatus() != GameStatus.SCHEDULED) {
                throw new IllegalArgumentException("경기 [ID: " + dto.getGameId() + "]는 예측 가능한 상태가 아닙니다. 현재 상태: " + todayGame.getGameStatus());
            }

            // 7. 사용자가 선택한 팀 엔티티를 찾습니다.
            Team chosenTeam = null;
            if (dto.getUserChoiceTeamId().equals(todayGame.getHomeTeam().getId())) {
                chosenTeam = todayGame.getHomeTeam();
            } else if (dto.getUserChoiceTeamId().equals(todayGame.getAwayTeam().getId())) {
                chosenTeam = todayGame.getAwayTeam();
            }

            // 8. UserPrediction 엔티티를 빌드하여 리스트에 추가합니다.
            UserPrediction userPrediction = UserPrediction.builder()
                    .user(user)
                    .game(todayGame)
                    .userChoiceTeam(chosenTeam)
                    .result(PredictionStatus.WAITING)
                    .build();

            userPredictionsToSave.add(userPrediction);
        }

        // 9. Repository의 saveAll 메서드를 호출하여 모든 UserPrediction 엔티티를 저장합니다.
        userPredictionRepository.saveAll(userPredictionsToSave);

        return saveDTO; // 요청 DTO 목록 반환
    }
}