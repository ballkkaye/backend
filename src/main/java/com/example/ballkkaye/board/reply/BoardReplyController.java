package com.example.ballkkaye.board.reply;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardReplyController {
    private final BoardReplyService boardReplyService;
    private final UserRepository userRepository; // 지워야함

    // 댓글 작성
    @PostMapping("/s/api/boards/{id}/reply")
    public ResponseEntity<?> save(@PathVariable("id") Integer id,
                                  @RequestBody BoardReplyRequest.SaveDTO reqDTO) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        var respDTO = boardReplyService.save(id, sessionUser, reqDTO);
        return Resp.ok(respDTO);
    }
}