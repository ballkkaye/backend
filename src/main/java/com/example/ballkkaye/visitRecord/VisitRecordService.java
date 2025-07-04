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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<VisitRecordResponse.ItemDTO> imgs = new ArrayList<>();
        for (String img : reqDTO.getImages()) {
            VisitRecordImage visitRecordImage = new VisitRecordImage()
                    .builder()
                    .visitRecord(visitRecord)
                    .imageUrl(img)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .build();
            visitRecordImageRepository.save(visitRecordImage);
            imgs.add(new VisitRecordResponse.ItemDTO(
                    visitRecordImage.getId(),
                    visitRecordImage.getImageUrl()
            ));
        }

        return new VisitRecordResponse.DTO(visitRecord, imgs);
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

        // 2. 이미지 먼저 처리
        if (reqDTO.getNewImages().size() + reqDTO.getRemainImageUrls().size() > 10) {
            throw new RuntimeException("이미지 너무 많음");
        }

        List<VisitRecordImage> images = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecordPS, DeleteStatus.NOT_DELETED);

        // 3. 기존 기록을 삭제 상태로 변경
        for (VisitRecordImage image : images) {
            if (!reqDTO.getRemainImageUrls().contains(image.getImageUrl())) {
                image.delete();
            }
        }

        // 4. 새로운 이미지 저장
        for (String img : reqDTO.getNewImages()) {
            VisitRecordImage visitRecordImage = new VisitRecordImage()
                    .builder()
                    .visitRecord(visitRecordPS)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .imageUrl(img)
                    .build();
            visitRecordImageRepository.save(visitRecordImage);
        }


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
        List<VisitRecordImage> newImagesUrl = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecordPS, DeleteStatus.NOT_DELETED);
        List<VisitRecordResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (VisitRecordImage image : newImagesUrl) {
            itemDTOS.add(new VisitRecordResponse.ItemDTO(image.getId(), image.getImageUrl()));
        }
        return new VisitRecordResponse.DTO(newRecord, itemDTOS);
    }


    public VisitRecordResponse.DTO getOne(Integer id, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다"));

        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        // 2. 이미지 조회
        List<VisitRecordImage> images = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecordPS, DeleteStatus.NOT_DELETED);

        List<VisitRecordResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (VisitRecordImage image : images) {
            itemDTOS.add(new VisitRecordResponse.ItemDTO(image.getId(), image.getImageUrl()));
        }

        return new VisitRecordResponse.DTO(visitRecordPS, itemDTOS);
    }


    public List<VisitRecordResponse.ListDTO> getList(Integer sessionUserId, LocalDate date, Integer year, Integer month) {
        List<VisitRecord> visitRecords;

        if (date != null) {
            visitRecords = visitRecordRepository.findAllByUserIdAndDate(sessionUserId, date);
        } else if (year != null && month != null) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            visitRecords = visitRecordRepository.findAllByUserIdAndMonth(sessionUserId, start, end);
        } else {
            throw new IllegalArgumentException("날짜 또는 년월 정보가 필요합니다.");
        }

        return visitRecords.stream()
                .map(VisitRecordResponse.ListDTO::new)
                .toList();
    }


    public List<LocalDate> getHighlightDates(Integer sessionUserId, Integer year, Integer month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        Timestamp startTimestamp = Timestamp.valueOf(start.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(end.plusDays(1).atStartOfDay().minusSeconds(1));

        List<Date> sqlDates = visitRecordRepository.findDistinctDatesByUserIdAndMonth(sessionUserId, startTimestamp, endTimestamp);


        return sqlDates.stream()
                .map(Date::toLocalDate)
                .toList();
    }


    public VisitRecordResponse.DetailDTO getDetail(Integer id, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(id, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다."));

        // 2. 이미지 조회
        List<VisitRecordImage> images = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecordPS, DeleteStatus.NOT_DELETED);

        List<VisitRecordResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (VisitRecordImage image : images) {
            itemDTOS.add(new VisitRecordResponse.ItemDTO(image.getId(), image.getImageUrl()));
        }
        VisitRecordResponse.DetailDTO detailDTO = new VisitRecordResponse.DetailDTO(visitRecordPS, itemDTOS);

        return detailDTO;
    }


    @Transactional
    public Object delete(Integer visitRecordId, Integer sessionUserId) {
        // 1. 직관기록 조회
        VisitRecord visitRecordPS = visitRecordRepository.findByIdAndUserId(visitRecordId, sessionUserId)
                .orElseThrow(() -> new RuntimeException("직관기록을 찾을 수 없습니다."));

        // 2. 직관기록 이미지 조회
        List<VisitRecordImage> images = visitRecordImageRepository.findByVisitRecordIdAndDeleteStatus(visitRecordPS, DeleteStatus.NOT_DELETED);

        // 권한 확인
        if (!visitRecordPS.getUser().getId().equals(sessionUserId)) {
            throw new RuntimeException("권한이 없습니다");
        }

        // 직관기록 삭제
        visitRecordPS.delete();
        // 이미지 삭제
        for (VisitRecordImage image : images) {
            image.delete();
        }

        return new VisitRecordResponse.DeleteDTO();
    }
}