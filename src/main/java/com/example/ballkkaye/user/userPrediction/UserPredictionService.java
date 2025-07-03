package com.example.ballkkaye.user.userPrediction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPredictionService {

    private final UserPredictionRepository userPredictionRepository;

    public List<UserPredictionResponse.TodayGameDTO> getTodayGames(Integer userId) {
        LocalDate today = LocalDate.now();
        List<UserPredictionResponse.TodayGameDTO> rawList =
                userPredictionRepository.findTodayGamesForPrediction(today, userId);

        List<UserPredictionResponse.TodayGameDTO> finalList = new ArrayList<>();

        for (UserPredictionResponse.TodayGameDTO raw : rawList) {
            // count 값을 가져옴 (초기 상태는 투표 수)
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

            // 비율로 덮어쓰기
            raw.setHomeVoteRate(homeRate);
            raw.setAwayVoteRate(awayRate);

            finalList.add(raw);
        }

        return finalList;
    }
}
