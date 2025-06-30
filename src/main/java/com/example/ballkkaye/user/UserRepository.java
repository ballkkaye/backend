package com.example.ballkkaye.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
