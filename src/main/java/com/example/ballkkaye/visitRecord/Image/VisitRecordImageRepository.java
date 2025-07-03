package com.example.ballkkaye.visitRecord.Image;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VisitRecordImageRepository {
    private final EntityManager em;


    // 직관기록 이미지 저장
    public VisitRecordImage save(VisitRecordImage visitRecordImage) {
        em.persist(visitRecordImage);
        return visitRecordImage;
    }
}
