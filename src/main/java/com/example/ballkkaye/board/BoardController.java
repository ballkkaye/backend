package com.example.ballkkaye.board;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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

    // 게시글 수정
    @PutMapping("/s/api/boards/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody BoardRequest.UpdateDTO reqDTO,
                                    @PathVariable("id") Integer id,
                                    Errors errors) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        BoardResponse.UpdateDTO respDTO = boardService.update(reqDTO, sessionUser, id);
        return Resp.ok(respDTO);
    }

    // 게시글 목록
    @GetMapping("/s/api/boards")
    public ResponseEntity<?> getBoards(@RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(required = false, value = "teamId") Integer teamId
    ) {
        //        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        BoardResponse.ListDTO respDTO = boardService.getBoards(teamId, page);

        return Resp.ok(respDTO);
    }

    // 게시글 상세보기 조회 TODO 댓글 + 좋아요 완성하고 테스트 해봐야함
    @GetMapping("/s/api/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        //        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        BoardResponse.DetailDTO respDTO = boardService.detail(id, sessionUser);
        return Resp.ok(respDTO);
    }

    @PostMapping("/s/api/boards/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        //        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        boardService.delete(id, sessionUser);
        return Resp.ok(null);
    }
}