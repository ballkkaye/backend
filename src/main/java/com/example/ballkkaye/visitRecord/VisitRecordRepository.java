package com.example.ballkkaye.visitRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    // NOT_DELETED 상태의 직관기록 단건 조회
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


    // NOT_DELETED 상태의 특정 월에 해당하는 직관 기록 목록 조회
    public List<VisitRecord> findAllByUserIdAndMonth(Integer userId, LocalDate start, LocalDate end) {
        return em.createQuery("""
                            select v from VisitRecord v
                            join fetch v.user
                            where v.user.id = :userId
                            and v.deleteStatus = 'NOT_DELETED'
                            and v.createdAt between :start and :end
                            order by v.id desc
                        """, VisitRecord.class)
                .setParameter("userId", userId)
                .setParameter("start", Timestamp.valueOf(start.atStartOfDay()))
                .setParameter("end", Timestamp.valueOf(end.plusDays(1).atStartOfDay().minusSeconds(1))) // inclusive
                .getResultList();
    }


    // NOT_DELETED 상태의 특정 날짜에 해당하는 직관 기록 목록 조회
    public List<VisitRecord> findAllByUserIdAndDate(Integer userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime exclusiveEnd = date.plusDays(1).atStartOfDay();

        return em.createQuery("""
                            select v from VisitRecord v
                            join fetch v.user
                            where v.user.id = :userId
                              and v.deleteStatus = 'NOT_DELETED'
                              and v.createdAt >= :start and v.createdAt < :end
                            order by v.id desc
                        """, VisitRecord.class)
                .setParameter("userId", userId)
                .setParameter("start", Timestamp.valueOf(startOfDay))
                .setParameter("end", Timestamp.valueOf(exclusiveEnd))
                .getResultList();
    }

    // 특정 유저가 해당 월에 직관 기록 목록을 작성한 날짜들만 중복 없이 조회 (NOT_DELETED 상태만)
    public List<Date> findDistinctDatesByUserIdAndMonth(Integer userId, Timestamp start, Timestamp end) {
        return em.createQuery("""
                            select distinct cast(v.createdAt as date)
                            from VisitRecord v
                            where v.user.id = :userId
                              and v.deleteStatus = 'NOT_DELETED'
                              and v.createdAt between :start and :end
                        """, Date.class)
                .setParameter("userId", userId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
