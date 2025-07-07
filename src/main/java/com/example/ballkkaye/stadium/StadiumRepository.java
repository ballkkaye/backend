package com.example.ballkkaye.stadium;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class StadiumRepository {
    private final EntityManager em;


    public List<Stadium> findAll() {
        return em.createQuery("SELECT s FROM Stadium s", Stadium.class)
                .getResultList();
    }
}
