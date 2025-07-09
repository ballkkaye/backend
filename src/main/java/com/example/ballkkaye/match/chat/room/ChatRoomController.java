package com.example.ballkkaye.match.chat.room;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final HttpSession session;

    // 채팅방 삭제
    @DeleteMapping("/s/api/chatrooms/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatRoomService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }


    // 채팅방 목록
    @GetMapping("/s/api/chatrooms")
    public ResponseEntity<?> chatrooms() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatRoomService.chatrooms(sessionUser);
        return Resp.ok(respDTO);
    }
}