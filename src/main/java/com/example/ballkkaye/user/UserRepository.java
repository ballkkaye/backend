package com.example.ballkkaye.user;

import com.example.ballkkaye.common.enums.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    public Optional<User> findById(Integer userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    public Optional<User> findByEmail(String email) {
        String q = "SELECT u FROM User u WHERE u.email = :email";
        return em.createQuery(q, User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        String q = "SELECT u FROM User u WHERE u.username = :username";
        return em.createQuery(q, User.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String q = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber";
        return em.createQuery(q, User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<User> findByNickname(String nickname) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // UserRole.USER 이고 fcmToken이 null이 아닌 사용자들의 FCM 토큰 목록 조회
    public List<String> findFcmTokensByRole(UserRole role) {
        return em.createQuery("""
                            SELECT u.fcmToken
                            FROM User u
                            WHERE u.userRole = :role
                              AND u.fcmToken IS NOT NULL
                              AND u.fcmToken <> ''
                        """, String.class)
                .setParameter("role", role)
                .getResultList();
    }
}
