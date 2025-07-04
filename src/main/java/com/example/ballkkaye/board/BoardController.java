package com.example.ballkkaye.board;

import com.example.ballkkaye._core.util.Resp;
import com.example.ballkkaye.user.User;
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

    // 커뮤니티 게시글 등록
    @PostMapping("/s/api/boards")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.SaveDTO respDTO = boardService.save(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    // 게시글 수정
    @PutMapping("/s/api/boards/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody BoardRequest.UpdateDTO reqDTO,
                                    @PathVariable("id") Integer id,
                                    Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.UpdateDTO respDTO = boardService.update(reqDTO, sessionUser, id);
        return Resp.ok(respDTO);
    }

    // 게시글 목록
    @GetMapping("/s/api/boards")
    public ResponseEntity<?> getBoards(@RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(required = false, value = "teamId") Integer teamId
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.ListDTO respDTO = boardService.getBoards(teamId, page);

        return Resp.ok(respDTO);
    }

    // 게시글 상세보기 조회
    @GetMapping("/s/api/boards/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO respDTO = boardService.detail(id, sessionUser);
        return Resp.ok(respDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/s/api/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTO = boardService.delete(id, sessionUser);
        return Resp.ok(respDTO);
    }
}