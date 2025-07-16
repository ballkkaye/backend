package com.example.ballkkaye.board.reply.like;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.board.like.BoardLikeResponse;
import com.example.ballkkaye.board.reply.BoardReply;
import com.example.ballkkaye.board.reply.BoardReplyRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardReplyLikeService {
    private final BoardReplyLikeRepository boardReplyLikeRepository;
    private final UserRepository userRepository;
    private final BoardReplyRepository boardReplyRepository;

    // 댓글 좋아요
    @Transactional
    public Object save(Integer replyId, User sessionUser) {
        log.info("[댓글 좋아요 요청] replyId={}, userId={}", replyId, sessionUser.getId());

        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // 2. 게시글이 존재하는지 확인
        BoardReply boardReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 댓글: replyId={}", replyId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // 3. 이미 존재하는 좋아요인지 검색
        if (boardReplyLikeRepository.findByReplyIdAndUserId(replyId, userPS.getId()).isPresent()) {
            log.warn("이미 좋아요한 댓글: replyId={}, userId={}", replyId, userPS.getId());
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        // 4. 없으면 저장
        BoardReplyLike boardReplyLike = new BoardReplyLike(boardReplyPS, userPS);
        boardReplyLikeRepository.save(boardReplyLike);

        // 5. 좋아요 수 조회
        Integer boardLikeCount = boardReplyLikeRepository.findTotalCount(replyId);

        log.info("[댓글 좋아요 저장 완료] replyLikeId={}, replyId={}, userId={}, count={}",
                boardReplyLike.getId(), replyId, userPS.getId(), boardLikeCount);

        BoardReplyLikeResponse.SaveDTO respDTO = new BoardReplyLikeResponse.SaveDTO(boardReplyLike, boardLikeCount);

        // 5. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }

    @Transactional
    public Object delete(Integer likeId, User sessionUser) {
        log.info("[댓글 좋아요 삭제 요청] likeId={}, userId={}", likeId, sessionUser.getId());

        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. board/reply/like 조회
        BoardReplyLike boardReplyLikePS = boardReplyLikeRepository.findById(likeId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 댓글 좋아요: likeId={}", likeId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 주인인지 확인
        if (boardReplyLikePS.getUser().getId() != userPS.getId()) {
            log.warn("댓글 좋아요 삭제 권한 없음: likeId={}, 요청자={}, 소유자={}",
                    likeId, userPS.getId(), boardReplyLikePS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        boardReplyLikeRepository.deleteById(boardReplyLikePS.getId());
        log.info("댓글 좋아요 삭제 완료: likeId={}, replyId={}, userId={}",
                likeId, boardReplyLikePS.getBoardReply().getId(), userPS.getId());

        // 5. 좋아요 갯수 반환
        Integer boardLikeCount = boardReplyLikeRepository.findTotalCount(boardReplyLikePS.getId());

        log.info("남은 댓글 좋아요 수: replyId={}, count={}",
                boardReplyLikePS.getBoardReply().getId(), boardLikeCount);

        // 6. DTO에 옮기기
        BoardLikeResponse.DeleteDTO respDTO = new BoardLikeResponse.DeleteDTO(boardLikeCount);

        return respDTO;
    }
}
