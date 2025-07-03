package com.example.ballkkaye.visitRecord.Image;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VisitRecordImageRepository {
    private final EntityManager em;


    // 직관기록 이미지 저장
    public VisitRecordImage save(VisitRecordImage visitRecordImage) {
        em.persist(visitRecordImage);
        return visitRecordImage;
    }


    // 직관기록 이미지 상태 조회 (NOT_DELETED 상태만)
    public Optional<VisitRecordImage> findByVisitRecordId(Integer visitRecordId) {
        try {
            VisitRecordImage result = em.createQuery("""
                            SELECT v FROM VisitRecordImage v
                            WHERE v.visitRecordId = :visitRecordId
                              AND v.deleteStatus = com.example.ballkkaye.common.enums.DeleteStatus.NOT_DELETED
                            """, VisitRecordImage.class)
                    .setParameter("visitRecordId", visitRecordId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
