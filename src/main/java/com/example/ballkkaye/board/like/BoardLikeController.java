package com.example.ballkkaye.board.like;

import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardLikeController {
    private final BoardLikeService boardLikeService;
    private final UserRepository userRepository; // 지워야함

    @PostMapping("/s/api/boards/like/{id}")
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = userRepository.findByEmail("ssar@nate.com")
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다"));
        var respDTO = boardLikeService.save(id, sessionUser);
        return ResponseEntity.ok(respDTO);
    }
}
