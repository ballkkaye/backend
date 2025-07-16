package com.example.ballkkaye._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi401;
import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String accessToken = request.getHeader("Authorization");

        try {
            if (accessToken == null || accessToken.isBlank()) throw new ExceptionApi400("토큰을 전달해주세요");
            if (!accessToken.startsWith("Bearer ")) throw new ExceptionApi401("유효하지 않은 토큰입니다.");

            accessToken = accessToken.replace("Bearer ", "");


            User user = JwtUtil.verify(accessToken);

            // 토큰을 다시 검증하기 귀찮아서, 임시로 세션에 넣어둔거다.
            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", user);

            chain.doFilter(request, response);
        } catch (TokenExpiredException e1) {
            log.error("JWT 만료 예외 발생", e1);
            exResponse(response, "토큰이 만료되었습니다");
        } catch (JWTDecodeException | SignatureVerificationException e2) {
            log.error("JWT 디코딩 또는 서명 검증 실패", e2);
            exResponse(response, "토큰 검증에 실패했어요");
        } catch (RuntimeException e3) {
            log.error("기타 런타임 예외 발생", e3);
            exResponse(response, e3.getMessage());
        }
    }

    private void exResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        PrintWriter out = response.getWriter();

        Resp<?> resp = Resp.fail(401, msg);
        String responseBody = new ObjectMapper().writeValueAsString(resp);
        out.println(responseBody);
    }
}