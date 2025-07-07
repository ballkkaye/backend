package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate sms;
    private final HttpSession session;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageRequest.DTO reqDTO) {
        System.out.println("======================");
        System.out.println(reqDTO.getAccessToken());
        System.out.println("======================");
        String token = reqDTO.getAccessToken();
        User sessionUser = JwtUtil.verify(token);
        chatMessageService.save(reqDTO, sessionUser);
        String destination = "/sub/chat/" + reqDTO.getRoomId();
        sms.convertAndSend(destination, reqDTO);
    }
}