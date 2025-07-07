package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomUserController {
    private final ChatRoomUserService chatRoomUserService;
    private final HttpSession session;

    // 채팅방 입장
    @PostMapping("/s/api/chatrooms/{id}")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatRoomUserService.save(id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 채팅방 퇴장
    @DeleteMapping("/s/api/chatrooms/user/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = chatRoomUserService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}