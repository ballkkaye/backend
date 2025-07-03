package com.example.ballkkaye.game.today;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodayGameService {
    private final TodayGameRepository todayGameRepository;

    // 유저 예측 결과 조회용
    public List<TodayGameResponse.PredictionDTO> getTodayGamePredictions() {
        return todayGameRepository.findTodayGameWithPitcherInfo();
    }

    // 날짜 기반 오늘 경기 리스트 조회
    public List<TodayGameResponse.ItemDTO> getTodayGames(LocalDate date) {
        return todayGameRepository.findTodayGameList(date);
//        2025-02-02 날짜 파싱해서 넘겨주자
    }
}
