package com.example.ballkkaye;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatTestController {

    @GetMapping("/chat-test")
    public String chatPage() {
        return "chat-test-view";
    }
}
