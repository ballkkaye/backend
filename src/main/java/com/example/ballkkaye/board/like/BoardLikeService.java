package com.example.ballkkaye.board.like;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
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

    // 게시글 좋아요 저장
    @Transactional
    public Object save(Integer boardId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 게시글 삭제 되었는지 확인
        if (boardPS.getDeleteStatus().equals(DeleteStatus.DELETED)) {
            throw new RuntimeException("해당 자원을 찾을 수 없습니다.");
        }

        // 4. 이미 존재하는 좋아요인지 검색
        if (boardLikeRepository.findByBoardIdAndUserId(boardId, userPS.getId()).isPresent()) {
            throw new RuntimeException("이미 좋아요 한 게시물입니다.");
        }

        // 5. 없으면 저장
        BoardLike boardLike = new BoardLike(boardPS, userPS);
        boardLikeRepository.save(boardLike);

        // 6. 좋아요 수 조회
        Integer boardLikeCount = boardLikeRepository.totalCount(boardId);

        BoardLikeResponse.SaveDTO respDTO = new BoardLikeResponse.SaveDTO(boardLike, boardLikeCount);

        // 7. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }

    @Transactional
    public BoardLikeResponse.DeleteDTO delete(Integer likeId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 2. board/like 조회
        BoardLike boardLikePS = boardLikeRepository.findById(likeId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 주인인지 확인
        if (boardLikePS.getUser().getId() != userPS.getId()) {
            throw new RuntimeException("해당 기능에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        boardLikeRepository.deleteById(boardLikePS.getId());

        // 5. 좋아요 갯수 반환
        Integer boardLikeCount = boardLikeRepository.totalCount(boardLikePS.getId());

        // 6. DTO에 옮기기
        BoardLikeResponse.DeleteDTO respDTO = new BoardLikeResponse.DeleteDTO(boardLikeCount);

        return respDTO;
    }
}
