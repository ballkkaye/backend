package com.example.ballkkaye.visitRecord.Image;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.visitRecord.VisitRecord;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class VisitRecordImageRepository {
    private final EntityManager em;


    // 직관기록 이미지 저장
    public VisitRecordImage save(VisitRecordImage visitRecordImage) {
        em.persist(visitRecordImage);
        return visitRecordImage;
    }

    public List<VisitRecordImage> findByVisitRecordIdAndDeleteStatus(VisitRecord visitRecordPS, DeleteStatus deleteStatus) {
        return em.createQuery(
                        "SELECT vri FROM VisitRecordImage vri WHERE vri.visitRecord = :visitRecord AND vri.deleteStatus = :deleteStatus",
                        VisitRecordImage.class)
                .setParameter("visitRecord", visitRecordPS)
                .setParameter("deleteStatus", deleteStatus)
                .getResultList();
    }
}
