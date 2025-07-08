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
//            String token = req.getHeader("Authorization");
            String token = req.getParameter("token");
            if (token == null) {//|| !token.startsWith("Bearer ")
                System.out.println(false);
                return false;
            }
            System.out.println(true);
            token = token.replace("Bearer ", "");
            try {
                User user = JwtUtil.verify(token);
                attributes.put("sessionUser", user);

                String roomIdParam = req.getParameter("roomId");
                if (roomIdParam != null) {
                    try {
                        Integer roomId = Integer.parseInt(roomIdParam);

                        boolean exists = chatRoomUserRepository.existsByUserIdAndChatRoomId(user.getId(), roomId);
                        if (!exists) return false;
                        attributes.put("roomId", roomId);
                    } catch (NumberFormatException ignored) {
                    }
                }

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

