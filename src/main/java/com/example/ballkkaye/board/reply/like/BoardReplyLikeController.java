package com.example.ballkkaye.board.reply.like;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardReplyLikeController {
    private final BoardReplyLikeService boardReplyLikeService;
    private final HttpSession session;

    // 좋아요 등록
    @PostMapping("/s/api/boards/reply/{id}/like")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyLikeService.save(id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 좋아요 삭제
    @DeleteMapping("/s/api/boards/reply/like/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyLikeService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
