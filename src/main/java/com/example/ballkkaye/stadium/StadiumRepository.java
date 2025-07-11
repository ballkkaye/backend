package com.example.ballkkaye.stadium;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class StadiumRepository {
    private final EntityManager em;


    // 구장 전체 조회
    public List<Stadium> findAll() {
        return em.createQuery("SELECT s FROM Stadium s", Stadium.class)
                .getResultList();
    }

    // 구장 단건 조회
    public Optional<Stadium> findById(Integer stadiumId) {
        return Optional.ofNullable(em.find(Stadium.class, stadiumId));
    }

    // 구장 저장
    public Stadium save(Stadium stadium) {
        em.persist(stadium);
        return stadium;
    }
}
