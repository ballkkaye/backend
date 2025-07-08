package com.example.ballkkaye.team.record.today;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodayTeamRecordService {
    private final TodayTeamRecordRepository todayTeamRecordRepository;

    // 모든 팀 기록을 DTO 형태로 반환하는 메서드
    public List<TodayTeamRecordResponse.DTO> teamRecords() {
        // 1. Repository를 통해 모든 TodayTeamRecord 엔티티를 조회합니다.
        List<TodayTeamRecord> teamRecords = todayTeamRecordRepository.findAll();

        // 2. 조회된 엔티티 리스트를 DTO 리스트로 변환합니다.
        return teamRecords.stream().map(record -> {
            TodayTeamRecordResponse.DTO DTO = new TodayTeamRecordResponse.DTO();
            DTO.setTeamName(record.getTeam().getTeamName());
            DTO.setGap(record.getGap());
            DTO.setWinGame(record.getWinGame());
            DTO.setLoseGame(record.getLoseGame());
            DTO.setTieGame(record.getTieGame());
            DTO.setTotalGame(record.getTotalGame());
            DTO.setWinRate(record.getWinRate());
            DTO.setTeamRank(record.getTeamRank());
            DTO.setRecentTenGame(record.getRecentTenGame());
            DTO.setStreak(record.getStreak());
            return DTO;
        }).collect(Collectors.toList());
    }
}
