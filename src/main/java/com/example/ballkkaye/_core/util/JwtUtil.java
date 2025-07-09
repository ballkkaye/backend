package com.example.ballkkaye._core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ballkkaye.common.enums.UserRole;
import com.example.ballkkaye.user.User;

import java.util.Date;

public class JwtUtil {
    public static String createRefresh(User user) {
        String jwt = JWT.create()
                .withSubject("refresh-token ")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("userrole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512("ballkkaye"));
        return jwt;
    }

    public static String create(User user) {
        String jwt = JWT.create()
                .withSubject("access-token")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("userrole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512("ballkkaye"));
        return jwt;
    }

    public static User verify(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("ballkkaye")).build().verify(jwt);
        int id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();
        String role = decodedJWT.getClaim("userrole").asString();
        UserRole userRole = UserRole.valueOf(role);

        return User.builder()
                .id(id)
                .username(username)
                .userRole(userRole)
                .build();
    }
}