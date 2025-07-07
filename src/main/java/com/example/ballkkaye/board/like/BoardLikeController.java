package com.example.ballkkaye.board.like;

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
public class BoardLikeController {
    private final BoardLikeService boardLikeService;
    private final HttpSession session;

    // 좋아요 등록
    @PostMapping("/s/api/boards/{id}/like")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardLikeService.save(id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 좋아요 삭제
    @DeleteMapping("/s/api/boards/like/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardLikeResponse.DeleteDTO respDTO = boardLikeService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
