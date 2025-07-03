package com.example.ballkkaye.visitRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VisitRecordRepository {
    private final EntityManager em;


    // 직관기록 저장
    public VisitRecord save(VisitRecord visitRecord) {
        em.persist(visitRecord);
        return visitRecord;
    }

    // NOT_DELETED 상태의 직관기록 조회
    public Optional<VisitRecord> findByIdAndUserId(Integer id, Integer userId) {
        try {
            VisitRecord result = em.createQuery("""
                            select v from VisitRecord v
                            where v.id = :id
                              and v.user.id = :userId
                              and v.deleteStatus = com.example.ballkkaye.common.enums.DeleteStatus.NOT_DELETED
                            """, VisitRecord.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
