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

    public List<Match> findAll(int page, Gender gender, Age age, Integer teamId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM Match m WHERE m.deleteStatus = 'NOT_DELETED'");

        if (gender != Gender.NONE) {
            sb.append(" AND m.chatRoom.preferredGender = :gender");
        }
        if (age != Age.NONE) {
            sb.append(" AND m.chatRoom.preferredAge = :age");
        }
        if (teamId != null) {
            sb.append(" AND m.chatRoom.team.id = :teamId");
        }


        sb.append(" ORDER BY m.createdAt DESC");

        Query query = em.createQuery(sb.toString());

        if (gender != Gender.NONE) {
            query.setParameter("gender", Gender.valueOf(gender.toString()));
        }
        if (age != Age.NONE) {
            query.setParameter("age", Age.valueOf(age.toString()));
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

    public Optional<Match> findByChatRoomId(Integer chatRoomId) {
        String q = "SELECT m FROM Match m WHERE m.chatRoom.id = :chatRoomId AND m.deleteStatus = 'NOT_DELETED'";

        try {
            Match match = em.createQuery(q, Match.class)
                    .setParameter("chatRoomId", chatRoomId)
                    .getSingleResult();
            return Optional.of(match);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }
}
