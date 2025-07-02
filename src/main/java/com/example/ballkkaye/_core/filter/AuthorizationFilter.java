package com.example.ballkkaye._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");

        try {
            // 1. accessToken null 또는 형식 오류
            if (accessToken == null || !accessToken.startsWith("Bearer ")) {
                throw new RuntimeException("올바른 AccessToken이 필요합니다");
            }

            accessToken = accessToken.replace("Bearer ", "");

            // 2. accessToken 검증 시도
            User user = JwtUtil.verify(accessToken);

            // 3. 세션 저장 후 통과
            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", user);
            chain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            // 4. accessToken 만료 → refreshToken 검증
            if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
                redirectToLogin(response);
                return;
            }

            try {
                refreshToken = refreshToken.replace("Bearer ", "");
                User user = JwtUtil.verify(refreshToken); // refreshToken도 User 정보 담고 있다고 가정

                // 5. 재발급
                String newAccessToken = JwtUtil.create(user);
                response.setHeader("Authorization", "Bearer " + newAccessToken);

                // 6. 세션 저장
                HttpSession session = request.getSession();
                session.setAttribute("sessionUser", user);
                chain.doFilter(request, response);
            } catch (TokenExpiredException | SignatureVerificationException | JWTDecodeException ex) {
                redirectToLogin(response);
            }
        } catch (RuntimeException e) {
            exResponse(response, "토큰 검증 실패: " + e.getMessage());
        }
    }

    private void exResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        PrintWriter out = response.getWriter();
        Resp<?> resp = Resp.fail(401, msg);
        out.println(new ObjectMapper().writeValueAsString(resp));
    }

    private void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
    }
}
