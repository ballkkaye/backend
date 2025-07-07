package com.example.ballkkaye.board.reply;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoardReplyController {
    private final BoardReplyService boardReplyService;
    private final HttpSession session;

    // 댓글 작성
    @PostMapping("/s/api/boards/{id}/reply")
    public ResponseEntity<?> save(@PathVariable("id") Integer id,
                                  @RequestBody BoardReplyRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyService.save(id, sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/s/api/boards/reply/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 댓글 수정
    @PutMapping("/s/api/boards/reply/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody BoardReplyRequest.UpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyService.update(reqDTO, id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 특정 게시글 댓글 조회
    @GetMapping("/s/api/boards/{id}/reply")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardReplyService.detail(id, sessionUser);
        return Resp.ok(respDTO);
    }
}