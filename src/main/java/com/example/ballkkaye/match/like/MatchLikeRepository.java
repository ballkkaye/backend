package com.example.ballkkaye.match.like;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MatchLikeRepository {
    private final EntityManager em;

    public Optional<MatchLike> findByMatchIdAndUserId(Integer matchId, Integer userId) {
        try {
            MatchLike result = em.createQuery("""
                            SELECT ml FROM MatchLike ml
                            WHERE ml.match.id = :matchId AND ml.user.id = :userId
                            """, MatchLike.class)
                    .setParameter("matchId", matchId)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void save(MatchLike matchLike) {
        em.merge(matchLike);
    }

    public Integer totalCount(Integer matchId) {
        return em.createQuery(
                        "SELECT COUNT(mtl) FROM MatchLike mtl WHERE mtl.match.id = :matchId", Long.class)
                .setParameter("matchId", matchId)
                .getSingleResult().intValue();
    }
}
