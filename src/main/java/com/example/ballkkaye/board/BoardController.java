package com.example.ballkkaye.board;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;
    private final UserRepository userRepository; // 지워야함

    // 커뮤니티 게시글 등록
    @PostMapping("/s/api/boards")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO reqDTO, Errors errors) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        BoardResponse.SaveDTO respDTO = boardService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }
}