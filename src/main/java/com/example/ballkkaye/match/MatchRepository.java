package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MatchRepository {
    private final EntityManager em;

    public void save(Match match) {
        em.persist(match);
    }

    public List<Match> findAll(int page, String gender, String age, Integer teamId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM Match m WHERE m.deleteStatus = 'NOT_DELETED'");

        if (gender != null && !gender.isBlank()) {
            sb.append(" AND m.chatRoom.preferredGender = :gender");
        }
        if (age != null && !age.isBlank()) {
            sb.append(" AND m.chatRoom.preferredAge = :age");
        }
        if (teamId != null) {
            sb.append(" AND m.chatRoom.team.id = :teamId");
        }


        sb.append(" ORDER BY m.createdAt DESC");

        Query query = em.createQuery(sb.toString());

        if (gender != null && !gender.isBlank() && !(gender.equals(Gender.NONE.name()))) {
            query.setParameter("gender", Gender.valueOf(gender));
        }
        if (age != null && !age.isBlank() && !(age.equals(Age.NONE.name()))) {
            query.setParameter("age", Age.valueOf(age));
        }
        if (teamId != null) {
            query.setParameter("teamId", teamId);
        }


        query.setFirstResult(page * 5);
        query.setMaxResults(5);

        return query.getResultList();
    }

    public Optional<Match> findById(Integer matchId) {
        return Optional.ofNullable(em.find(Match.class, matchId));
    }
}
