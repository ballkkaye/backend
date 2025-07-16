package com.example.ballkkaye._core.interceptor;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.user.User;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
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

        String authHeader = request.getHeaders().getFirst("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "");
        } else if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            token = req.getParameter("token");
        }

        try {
            if (token == null) return false;

            User user = JwtUtil.verify(token);

            if (request instanceof ServletServerHttpRequest servletRequest) {
                HttpServletRequest req = servletRequest.getServletRequest();
                String roomIdParam = req.getParameter("roomId");

                if (roomIdParam != null) {
                    Integer roomId = Integer.parseInt(roomIdParam);
                    boolean exists = chatRoomUserRepository.existsByUserIdAndChatRoomId(user.getId(), roomId);
                    if (!exists) return false;

                    attributes.put("roomId", roomId);
                }
            }

            attributes.put("sessionUser", user);
            return true;

        } catch (Exception e) {
            Sentry.captureException(e);
            log.error("WebSocket beforeHandshake 실패: {}", e.getMessage(), e);
            return false;
        }
    }


    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}

