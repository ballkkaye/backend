package com.example.ballkkaye._core.eventlistener;

import com.example.ballkkaye.match.chat.room.ChatRoomService;
import com.example.ballkkaye.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final ChatRoomService chatRoomService;

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headers.getSessionAttributes();

        if (sessionAttributes == null) return;

        User sessionUser = (User) sessionAttributes.get("sessionUser");
        Integer roomId = (Integer) sessionAttributes.get("roomId");

        if (sessionUser != null && roomId != null) {
            chatRoomService.handleUserLeft(sessionUser, roomId);
        }
    }
}
