package com.example.ballkkaye.match.chat.room.user;

import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomUserController {
    private final ChatRoomUserService chatRoomUserService;
    private final HttpSession session;

    //채팅방 입장
    @PostMapping("/s/api/chat-room/")
    public ResponseEntity<?> save(@RequestParam("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        chatRoomUserService.save(id, sessionUser);
        return null;
    }
}