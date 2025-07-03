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
    public List<TodayTeamRecordResponse.DTO> getAllTeamRecords() {
        // 1. Repository를 통해 모든 TodayTeamRecord 엔티티를 조회합니다.
        List<TodayTeamRecord> teamRecords = todayTeamRecordRepository.findAll();

        // 2. 조회된 엔티티 리스트를 DTO 리스트로 변환합니다.
        return teamRecords.stream().map(record -> {
            TodayTeamRecordResponse.DTO dto = new TodayTeamRecordResponse.DTO();
            dto.setTeamName(record.getTeam().getTeamName());
            dto.setGap(record.getGap());
            dto.setWinGame(record.getWinGame());
            dto.setLoseGame(record.getLoseGame());
            dto.setTieGame(record.getTieGame());
            dto.setTotalGame(record.getTotalGame());
            dto.setWinRate(record.getWinRate());
            dto.setTeamRank(record.getTeamRank());
            dto.setRecentTenGame(record.getRecentTenGame());
            dto.setStreak(record.getStreak());
            return dto;
        }).collect(Collectors.toList());
    }
}
