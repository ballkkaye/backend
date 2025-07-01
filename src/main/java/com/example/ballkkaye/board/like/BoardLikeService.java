package com.example.ballkkaye.board.like;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Object save(Integer boardId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        // 3. 이미 존재하는 좋아요인지 검색
        if (boardLikeRepository.findByBoardIdAndUserId(boardId, userPS.getId()).isPresent()) {
            throw new RuntimeException("이미 좋아요 한 게시물입니다.");
        }

        // 4. 없으면 저장
        BoardLike boardLike = new BoardLike(boardPS, userPS);
        boardLikeRepository.save(boardLike);

        // 5. 좋아요 수 조회
        Integer boardLikeCount = boardLikeRepository.totalCount(boardId);

        BoardLikeResponse.SaveDTO respDTO = new BoardLikeResponse.SaveDTO(boardLike, boardLikeCount);

        // 5. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }
}

