package com.example.ballkkaye.visitRecord.Image;

import com.example.ballkkaye._core.util.ImageUtil;
import com.example.ballkkaye.common.enums.DeleteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VisitRecordImageService {
    private final VisitRecordImageRepository visitRecordImageRepository;


    @Transactional
    public VisitRecordImage save(String base64String, Integer visitRecordId) {
        String imageUrl = ImageUtil.saveBase64VisitImage(base64String);

        VisitRecordImage entity = VisitRecordImage.builder()
                .visitRecordId(visitRecordId)
                .imageUrl(imageUrl)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        return visitRecordImageRepository.save(entity); // 저장 후 바로 리턴
    }

    @Transactional
    public VisitRecordImage update(String imageString, Integer visitRecordId) {
        // 1. 기존 이미지 조회 (NOT_DELETED 상태만)
        VisitRecordImage oldImage = visitRecordImageRepository.findByVisitRecordId(visitRecordId)
                .orElseThrow(() -> new RuntimeException("직관기록 이미지를 찾을 수 없습니다"));

        // 2. 기존 이미지 상태 변경 및 저장
        oldImage.delete(); // deleteStatus = DELETED 로 변경
        visitRecordImageRepository.save(oldImage);

        // 3. 새 이미지 저장
        String savedPath = ImageUtil.saveBase64VisitImage(imageString);
        VisitRecordImage newImage = VisitRecordImage.builder()
                .visitRecordId(visitRecordId)
                .imageUrl(savedPath)
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .build();

        return visitRecordImageRepository.save(newImage);
    }
}
