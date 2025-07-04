package com.example.ballkkaye.visitRecord;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImage;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImageRepository;
import com.example.ballkkaye.visitRecord.Image.VisitRecordImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VisitRecordService {
    private final VisitRecordRepository visitRecordRepository;
    private final VisitRecordImageRepository visitRecordImageRepository;
    private final VisitRecordImageService visitRecordImageService;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    @Transactional
    public VisitRecordResponse.DTO save(VisitRecordRequest.SaveDTO reqDTO, User sessionUser) {
        // user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));


        // game 조회
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다"));


        // team조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));

        // 직관기록 저장
        VisitRecord visitRecord = reqDTO.toEntity(userPS, gamePS, teamPS);
        visitRecordRepository.save(visitRecord);

        //  직관기록 이미지 저장
        VisitRecordImage savedImage = null;
        if (reqDTO.getImageString() != null && !reqDTO.getImageString().isBlank()) {
            savedImage = visitRecordImageService.save(reqDTO.getImageString(), visitRecord.getId());
        }
        return new VisitRecordResponse.DTO(visitRecord, savedImage);
    }


    @Transactional
    public VisitRecordResponse.DTO update(VisitRecordRequest.UpdateDTO reqDTO, Integer id, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다"));

        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        // 2. 이미지 먼저 처리 (기존 ID로)
        VisitRecordImage savedImage = null;
        if (reqDTO.getImageString() != null && !reqDTO.getImageString().isBlank()) {
            savedImage = visitRecordImageService.update(reqDTO.getImageString(), visitRecordPS.getId());
        }


        // 3. 기존 기록을 삭제 상태로 변경
        visitRecordPS.delete(); // DeleteStatus.DELETED 로 상태 변경

        // 4. 새 기록 생성 후 저장
        VisitRecord newRecord = VisitRecord.builder()
                .game(visitRecordPS.getGame())
                .team(visitRecordPS.getTeam())
                .user(visitRecordPS.getUser())
                .result(reqDTO.getResult())
                .content(reqDTO.getContent())
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();
        visitRecordRepository.save(newRecord);

        // 4. 새 이미지에도 새 visitRecordId 부여
        if (savedImage != null) {
            savedImage.updateVisitRecordId(newRecord.getId());
            visitRecordImageRepository.save(savedImage);
        }

        return new VisitRecordResponse.DTO(newRecord, savedImage);
    }


    public VisitRecordResponse.DTO getOne(Integer id, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다"));

        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        // 2. 이미지 조회
        VisitRecordImage image = visitRecordImageRepository
                .findByVisitRecordId(visitRecordPS.getId())
                .orElseThrow(() -> new RuntimeException("직관기록 이미지를 찾을 수 없습니다"));

        return new VisitRecordResponse.DTO(visitRecordPS, image);
    }


    @Transactional
    public void delete(Integer visitRecordId, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다."));

        // 2. 직관기록 이미지 조회
        VisitRecordImage imagePS = visitRecordImageRepository
                .findByVisitRecordId(visitRecordPS.getId())
                .orElseThrow(() -> new RuntimeException("직관기록 이미지를 찾을 수 없습니다"));

        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        // 직관기록 삭제
        visitRecordPS.delete();
        // 이미지 삭제
        imagePS.delete();
    }
}
