package com.example.ballkkaye.user.userPrediction;

import com.example.ballkkaye.common.enums.GameStatus;
import com.example.ballkkaye.game.today.TodayGame;
import com.example.ballkkaye.game.today.TodayGameRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
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
    private final TeamRepository teamRepository;

    @Transactional
    public List<UserPredictionResponse.SaveDTO> savePrediction(UserPredictionRequest.SaveDTO reqDTO) {
        List<UserPredictionResponse.SaveDTO> savedList = new ArrayList<>();

        User user = userRepository.findById(reqDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        for (UserPredictionRequest.DTO prediction : reqDTO.getUserPredictions()) {
            Integer gameId = prediction.getGameId();
            Integer teamId = prediction.getUserChoiceTeamId();

            TodayGame game = todayGameRepository.findById(gameId)
                    .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다"));

            if (game.getGameTime().toLocalDateTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("이미 시작된 경기는 예측할 수 없습니다: gameId = " + gameId);
            }

            if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                throw new IllegalArgumentException("이미 시작된 경기는 예측할 수 없습니다: gameId = " + gameId);
            }

            if (game.getGameStatus() == GameStatus.COMPLETED) {
                throw new IllegalArgumentException("종료된 경기는 예측할 수 없습니다: gameId = " + gameId);
            }

            if (game.getGameStatus() == GameStatus.CANCELLED) {
                throw new IllegalArgumentException("취소된 경기는 예측할 수 없습니다: gameId = " + gameId);
            }

            if (userPredictionRepository.existsByUserAndGame(user, gameId)) {
                throw new IllegalArgumentException("이미 예측한 경기입니다: gameId = " + gameId);
            }

            if (teamId != null &&
                    !teamId.equals(game.getHomeTeam().getId()) &&
                    !teamId.equals(game.getAwayTeam().getId())) {
                throw new IllegalArgumentException("해당 경기의 팀이 아닌 팀을 선택했습니다: gameId = " + gameId + ", teamId = " + teamId);
            }

            Team team = (teamId != null) ? teamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("선택한 팀을 찾을 수 없습니다")) : null;

            UserPrediction up = UserPrediction.builder()
                    .user(user)
                    .game(game)
                    .userChoiceTeam(team)
                    .build();

            savedList.add(new UserPredictionResponse.SaveDTO(userPredictionRepository.save(up)));
        }

        return savedList;
    }

    public UserPredictionResponse.TodayListResponse getTodayPredictions(Integer userId) {
        List<Object[]> rows = userPredictionRepository.findTodayPredictionsByUser(userId, LocalDate.now());
        List<UserPredictionResponse.TodayListDTO> dtos = new ArrayList<>();

        for (Object[] row : rows) {
            Integer gameId = (Integer) row[1];
            GameStatus status = GameStatus.valueOf((String) row[2]);
            Integer homeScore = (Integer) row[3];
            Integer awayScore = (Integer) row[4];

            Integer homeTeamId = (Integer) row[5];
            String homeTeamName = (String) row[6];
            String homeLogo = (String) row[7];

            Integer awayTeamId = (Integer) row[8];
            String awayTeamName = (String) row[9];
            String awayLogo = (String) row[10];

            Integer userChoiceTeamId = (Integer) row[11];
            String result = (String) row[12];

            Long homeCount = ((Number) row[13]).longValue();
            Long awayCount = ((Number) row[14]).longValue();
            Long total = homeCount + awayCount;

            Integer homePer = (total > 0) ? Math.toIntExact(Math.round((homeCount * 100.0) / total)) : null;
            Integer awayPer = (total > 0) ? Math.toIntExact(Math.round((awayCount * 100.0) / total)) : null;

            UserPredictionResponse.TeamInfo homeInfo = new UserPredictionResponse.TeamInfo();
            homeInfo.setTeamId(homeTeamId);
            homeInfo.setTeamName(homeTeamName);
            homeInfo.setLogoImgUrl(homeLogo);
            homeInfo.setScore(status.isCompleted() ? homeScore : null);

            UserPredictionResponse.TeamInfo awayInfo = new UserPredictionResponse.TeamInfo();
            awayInfo.setTeamId(awayTeamId);
            awayInfo.setTeamName(awayTeamName);
            awayInfo.setLogoImgUrl(awayLogo);
            awayInfo.setScore(status.isCompleted() ? awayScore : null);

            UserPredictionResponse.ChoicePercentage per = new UserPredictionResponse.ChoicePercentage();
            per.setHome(homePer);
            per.setAway(awayPer);

            UserPredictionResponse.TodayListDTO dto = new UserPredictionResponse.TodayListDTO();
            dto.setGameId(gameId);
            dto.setHomeTeam(homeInfo);
            dto.setAwayTeam(awayInfo);
            dto.setUserChoiceTeamId(userChoiceTeamId);
            dto.setChoicePer(per);

            if (status == GameStatus.CANCELLED) {
                dto.setResult("CANCELLED");
            } else if (status.isCompleted()) {
                dto.setResult(result);
            } else {
                dto.setResult(null);
            }

            dtos.add(dto);
        }

        return new UserPredictionResponse.TodayListResponse(dtos);
    }
}
