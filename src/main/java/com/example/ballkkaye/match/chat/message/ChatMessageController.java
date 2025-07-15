package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.ChatSessionManager;
import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRequest;
import com.example.ballkkaye.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate sms;
    private final ChatSessionManager chatSessionManager;

    @SendTo("/sub/chat")
    @MessageMapping("/chat/send")
    public ResponseEntity<?> sendMessage(@Payload ChatMessageRequest.DTO reqDTO,
                                         SimpMessageHeaderAccessor headerAccessor) {
        User sessionUser = (User) headerAccessor.getSessionAttributes().get("sessionUser");
        ChatMessageResponse.DTO respDTO = chatMessageService.save(reqDTO, sessionUser);
        sms.convertAndSend("/sub/chat/" + respDTO.getChatRoomId(), respDTO);
        return Resp.ok(respDTO);
    }

    @MessageMapping("/chat/auth")
    public void handleAuth(@Payload ChatRoomUserRequest.AuthDTO reqDTO,
                           SimpMessageHeaderAccessor headerAccessor) {
        User sessionUser = (User) headerAccessor.getSessionAttributes().get("sessionUser");
        var respDTO = chatMessageService.handleAuth(reqDTO, sessionUser);

        sms.convertAndSend("/sub/chat/" + reqDTO.getRoomId(), respDTO);
    }

    @MessageMapping("/chat/unsubscribe")
    public void handleUnsubscribe(@Payload ChatRoomUserRequest.AuthDTO reqDTO,
                                  SimpMessageHeaderAccessor headerAccessor) {
        User sessionUser = (User) headerAccessor.getSessionAttributes().get("sessionUser");
        var respDTO = chatMessageService.unsubscribe(sessionUser, reqDTO);
        // ChatSessionManager에서 유저 제거
        sms.convertAndSend("/sub/chat/" + reqDTO.getRoomId(), respDTO);
    }
}