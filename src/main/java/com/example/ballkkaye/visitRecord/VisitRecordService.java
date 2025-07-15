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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
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
        log.info("직관기록 등록 요청 - userId: {}, gameId: {}, teamId: {}, imgUrl: '{}'",
                sessionUser.getId(), reqDTO.getGameId(), reqDTO.getTeamId(), reqDTO.getImgUrl());

        // user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저로 직관기록 등록 시도 - userId: {}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // game 조회
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 경기로 직관기록 등록 시도 - gameId: {}", reqDTO.getGameId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // team조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 팀으로 직관기록 등록 시도 - teamId: {}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // 직관기록 저장
        VisitRecord visitRecord = reqDTO.toEntity(userPS, gamePS, teamPS, reqDTO.getImgUrl());
        visitRecordRepository.save(visitRecord);

        log.info("직관기록 등록 완료 - visitRecordId: {}, userId: {}, gameId: {}, team: {}",
                visitRecord.getId(), userPS.getId(), gamePS.getId(), teamPS.getTeamName());

        return new VisitRecordResponse.DTO(visitRecord);
    }


    // 직관기록 수정
    @Transactional
    public VisitRecordResponse.DTO update(VisitRecordRequest.UpdateDTO reqDTO, Integer id, Integer sessionUserId) {
        log.info("직관기록 수정 요청 - visitRecordId: {}, userId: {}", id, sessionUserId);


        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않거나 권한 없는 직관기록 수정 시도 - visitRecordId: {}, userId: {}", id, sessionUserId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            log.warn("직관기록 수정 권한 없음 - visitRecordId: {}, 요청자 userId: {}, 실제 userId: {}",
                    id, sessionUserId, visitRecordPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 기존 직관기록 상태 변경
        visitRecordPS.delete();
        log.info("기존 직관기록 상태 변경 완료 - visitRecordId: {}", id);

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

        log.info("새로운 직관기록 등록 완료 - newVisitRecordId: {}, userId: {}", newRecord.getId(), sessionUserId);

        return new VisitRecordResponse.DTO(newRecord);
    }


    // 직관기록 목록
    public List<VisitRecordResponse.ListDTO> getList(Integer sessionUserId, LocalDate date, Integer year, Integer month) {
        log.info("직관기록 목록 조회 요청 - userId: {}, date: {}, year: {}, month: {}", sessionUserId, date, year, month);

        List<VisitRecord> visitRecords;
        if (date != null) {
            visitRecords = visitRecordRepository.findAllByUserIdAndDate(sessionUserId, date);
            log.info("단일 날짜 기준 직관기록 조회 - userId: {}, date: {}, 조회 수: {}", sessionUserId, date, visitRecords.size());
        } else if (year != null && month != null) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            visitRecords = visitRecordRepository.findAllByUserIdAndMonth(sessionUserId, start, end);
            log.info("월 기준 직관기록 조회 - userId: {}, 기간: {} ~ {}, 조회 수: {}", sessionUserId, start, end, visitRecords.size());

        } else {
            log.warn("직관기록 목록 조회 실패 - 잘못된 요청. userId: {}", sessionUserId);
            throw new ExceptionApi400("날짜 또는 년월 정보가 필요합니다.");
        }


        return visitRecords.stream()
                .map(VisitRecordResponse.ListDTO::new)
                .toList();
    }


    // 직관기록 상세보기
    public VisitRecordResponse.DetailDTO getDetail(Integer visitRecordId, Integer sessionUserId) {
        log.info("직관기록 상세 조회 요청 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);

        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 직관기록 상세 조회 시도 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            log.warn("직관기록 상세 조회 권한 없음 - visitRecordId: {}, 요청자 userId: {}, 실제 userId: {}",
                    visitRecordId, sessionUserId, visitRecordPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        VisitRecordResponse.DetailDTO detailDTO = new VisitRecordResponse.DetailDTO(visitRecordPS);

        log.info("직관기록 상세 조회 완료 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);


        return detailDTO;
    }


    // 직관기록 삭제
    @Transactional
    public Object delete(Integer visitRecordId, Integer sessionUserId) {
        log.info("직관기록 삭제 요청 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);


        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 직관기록 삭제 시도 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });
        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            log.warn("직관기록 삭제 권한 없음 - visitRecordId: {}, 요청자 userId: {}, 실제 userId: {}",
                    visitRecordId, sessionUserId, visitRecordPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 직관기록 삭제
        visitRecordPS.delete();
        log.info("직관기록 삭제 완료 - visitRecordId: {}, userId: {}", visitRecordId, sessionUserId);
        return new VisitRecordResponse.DeleteDTO();
    }
}
