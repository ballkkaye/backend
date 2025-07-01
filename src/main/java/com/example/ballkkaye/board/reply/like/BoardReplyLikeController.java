package com.example.ballkkaye.board.reply.like;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
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

    private final UserRepository userRepository; // 지워야함

    // 좋아요 등록
    @PostMapping("/s/api/boards/reply/{id}/like")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        var respDTO = boardReplyLikeService.save(id, sessionUser);
        return ResponseEntity.ok(respDTO);
    }

    // 좋아요 삭제
    @DeleteMapping("/s/api/boards/reply/like/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        var respDTO = boardReplyLikeService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}
