package com.example.ballkkaye.game.today;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TodayGameService {
    private final TodayGameRepository todayGameRepository;

    @Transactional
    public List<TodayGameResponse.PredictionDTO> getTodayGamePredictions() {
        return todayGameRepository.findTodayGameWithPitcherInfo();
    }

}
