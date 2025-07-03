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
}
