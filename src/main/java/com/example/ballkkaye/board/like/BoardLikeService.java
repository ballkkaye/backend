package com.example.ballkkaye.board.like;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 게시글 좋아요 저장
    @Transactional
    public Object save(Integer boardId, User sessionUser) {
        log.info("[좋아요 요청] boardId={}, userId={}", boardId, sessionUser.getId());

        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 게시글: boardId={}", boardId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 게시글 삭제 되었는지 확인
        if (boardPS.getDeleteStatus().equals(DeleteStatus.DELETED)) {
            log.warn("삭제된 게시글에 좋아요 요청: boardId={}", boardId);
            throw new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
        }

        // 4. 이미 존재하는 좋아요인지 검색
        if (boardLikeRepository.findByBoardIdAndUserId(boardId, userPS.getId()).isPresent()) {
            log.warn("이미 좋아요한 게시글: boardId={}, userId={}", boardId, userPS.getId());
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        // 5. 없으면 저장
        BoardLike boardLike = new BoardLike(boardPS, userPS);
        boardLikeRepository.save(boardLike);

        // 6. 좋아요 수 조회
        Integer boardLikeCount = boardLikeRepository.totalCount(boardId);
        log.info("[좋아요 저장 완료] boardLikeId={}, boardId={}, likeCount={}", boardLike.getId(), boardId, boardLikeCount);


        BoardLikeResponse.SaveDTO respDTO = new BoardLikeResponse.SaveDTO(boardLike, boardLikeCount);

        // 7. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }

    @Transactional
    public BoardLikeResponse.DeleteDTO delete(Integer likeId, User sessionUser) {
        log.info("[좋아요 삭제 요청] likeId={}, userId={}", likeId, sessionUser.getId());

        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. board/like 조회
        BoardLike boardLikePS = boardLikeRepository.findById(likeId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 좋아요: likeId={}", likeId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 주인인지 확인
        if (boardLikePS.getUser().getId() != userPS.getId()) {
            log.warn("좋아요 삭제 권한 없음: likeId={}, 요청자={}, 소유자={}",
                    likeId, userPS.getId(), boardLikePS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        boardLikeRepository.deleteById(boardLikePS.getId());
        log.info("좋아요 삭제 완료: likeId={}", likeId);

        // 5. 좋아요 갯수 반환
        Integer boardLikeCount = boardLikeRepository.totalCount(boardLikePS.getId());

        log.info("남은 좋아요 수: boardId={}, count={}",
                boardLikePS.getBoard().getId(), boardLikeCount);

        // 6. DTO에 옮기기
        BoardLikeResponse.DeleteDTO respDTO = new BoardLikeResponse.DeleteDTO(boardLikeCount);

        return respDTO;
    }
}
