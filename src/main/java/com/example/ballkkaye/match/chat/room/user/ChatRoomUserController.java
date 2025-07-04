package com.example.ballkkaye.match.chat.room.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomUserController {
    private final ChatRoomUserService chatRoomUserService;
    private final HttpSession session;


}