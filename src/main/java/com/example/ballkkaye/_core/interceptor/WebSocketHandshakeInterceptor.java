package com.example.ballkkaye._core.interceptor;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();

            // 1. JWT 토큰 파라미터 추출
            String token = req.getParameter("token");
            if (token == null) {
                return false;
            }

            try {
                // 2. 토큰 검증 및 유저 파싱
                token = token.replace("Bearer ", "");
                User user = JwtUtil.verify(token);

                // 3. 채팅방 참여 확인
                String roomIdParam = req.getParameter("roomId");
                if (roomIdParam != null) {
                    Integer roomId = Integer.parseInt(roomIdParam);
                    boolean exists = chatRoomUserRepository.existsByUserIdAndChatRoomId(user.getId(), roomId);
                    if (!exists) return false;
                    attributes.put("roomId", roomId);
                }

                // 4. WebSocket 세션에 사용자 정보 저장
                attributes.put("sessionUser", user);
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}

