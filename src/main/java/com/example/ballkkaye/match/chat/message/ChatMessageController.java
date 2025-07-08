package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.JwtUtil;
import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate sms;

    @SendTo("/sub/chat")
    @MessageMapping("/chat/send")
    public ResponseEntity<?> sendMessage(@Payload ChatMessageRequest.DTO reqDTO) {
        User sessionUser = JwtUtil.verify(reqDTO.getAccessToken());
        var respDTO = chatMessageService.save(reqDTO, sessionUser);
        sms.convertAndSend("/sub/chat/" + reqDTO.getRoomId(), reqDTO);
        return Resp.ok(respDTO);
    }
}