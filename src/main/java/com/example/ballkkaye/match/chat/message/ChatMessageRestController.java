package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;
    private final HttpSession session;

    @GetMapping("/s/api/chatrooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Integer roomId) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatMessageService.getMessages(roomId, sessionUser);
        return Resp.ok(respDTO);
    }
}
