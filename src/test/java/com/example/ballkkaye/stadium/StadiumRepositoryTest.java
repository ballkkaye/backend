package com.example.ballkkaye.stadium;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({StadiumRepository.class})
@DataJpaTest
public class StadiumRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Test
    public void findAll_test() {
        // when
        List<Stadium> result = stadiumRepository.findAll();

        // eye
        System.out.println("전체 구장 수: " + result.size());
        for (Stadium stadium : result) {
            System.out.println("구장 이름: " + stadium.getStadiumName());
        }
    }
}
