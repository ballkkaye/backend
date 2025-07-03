package com.example.ballkkaye.visitRecord;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VisitRecordRepository {
    private final EntityManager em;


    // 직관기록 저장
    public VisitRecord save(VisitRecord visitRecord) {
        em.persist(visitRecord);
        return visitRecord;
    }
}
