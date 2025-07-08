package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitRecordService {
    private final VisitRecordRepository visitRecordRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    // 직관기록 등록
    @Transactional
    public VisitRecordResponse.DTO save(VisitRecordRequest.SaveDTO reqDTO, User sessionUser) {
        // user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("유저를 찾을 수 없습니다"));


        // game 조회
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new ExceptionApi404("게임을 찾을 수 없습니다"));


        // team조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new ExceptionApi404("팀을 찾을 수 없습니다"));

        // 직관기록 저장
        VisitRecord visitRecord = reqDTO.toEntity(userPS, gamePS, teamPS, reqDTO.getImgUrl());
        visitRecordRepository.save(visitRecord);


        return new VisitRecordResponse.DTO(visitRecord);
    }


    // 직관기록 수정
    @Transactional
    public VisitRecordResponse.DTO update(VisitRecordRequest.UpdateDTO reqDTO, Integer id, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> new ExceptionApi404("직관기록을 찾을 수 없습니다"));

        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        // 기존 직관기록 상태 변경
        visitRecordPS.delete();

        // 4. 새로운 직관기록 생성
        VisitRecord newRecord = VisitRecord.builder()
                .user(visitRecordPS.getUser())
                .game(visitRecordPS.getGame())       // 기존 게임 그대로
                .team(visitRecordPS.getTeam())       // 기존 팀 그대로
                .result(reqDTO.getResult())
                .content(reqDTO.getContent())
                .imgUrl(reqDTO.getImgUrl())
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        // 4. 새 기록 생성 후 저장
        visitRecordRepository.save(newRecord);
        return new VisitRecordResponse.DTO(newRecord);
    }


    // 직관기록 목록
    public List<VisitRecordResponse.ListDTO> getList(Integer sessionUserId, LocalDate date, Integer year, Integer month) {
        List<VisitRecord> visitRecords;
        if (date != null) {
            visitRecords = visitRecordRepository.findAllByUserIdAndDate(sessionUserId, date);
        } else if (year != null && month != null) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            visitRecords = visitRecordRepository.findAllByUserIdAndMonth(sessionUserId, start, end);

        } else {
            throw new ExceptionApi400("날짜 또는 년월 정보가 필요합니다.");
        }


        return visitRecords.stream()
                .map(VisitRecordResponse.ListDTO::new)
                .toList();
    }


    // 직관기록 상세보기
    public VisitRecordResponse.DetailDTO getDetail(Integer visitRecordId, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> new ExceptionApi404("직관기록을 찾을 수 없습니다."));
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        VisitRecordResponse.DetailDTO detailDTO = new VisitRecordResponse.DetailDTO(visitRecordPS);

        return detailDTO;
    }


    // 직관기록 삭제
    @Transactional
    public Object delete(Integer visitRecordId, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> new ExceptionApi404("직관기록을 찾을 수 없습니다."));
        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        // 직관기록 삭제
        visitRecordPS.delete();
        return new VisitRecordResponse.DeleteDTO();
    }
}
