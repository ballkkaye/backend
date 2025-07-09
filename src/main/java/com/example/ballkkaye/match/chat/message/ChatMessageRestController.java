package com.example.ballkkaye.match.chat.message;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;
    private final HttpSession session;

    // 메세지 목록 조회
    @GetMapping("/s/api/chatrooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Integer roomId,
                                         @RequestParam(required = false, value = "page", defaultValue = "0") Integer page) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatMessageService.getMessages(roomId, sessionUser, page);
        return Resp.ok(respDTO);
    }

    // 메세지 삭제
    @DeleteMapping("/s/api/chatrooms/messages/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatMessageService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
