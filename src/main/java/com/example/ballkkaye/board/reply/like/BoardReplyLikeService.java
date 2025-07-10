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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardReplyLikeService {
    private final BoardReplyLikeRepository boardReplyLikeRepository;
    private final UserRepository userRepository;
    private final BoardReplyRepository boardReplyRepository;

    // 댓글 좋아요
    @Transactional
    public Object save(Integer replyId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 2. 게시글이 존재하는지 확인
        BoardReply boardReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 3. 이미 존재하는 좋아요인지 검색
        if (boardReplyLikeRepository.findByReplyIdAndUserId(replyId, userPS.getId()).isPresent()) {
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        // 4. 없으면 저장
        BoardReplyLike boardReplyLike = new BoardReplyLike(boardReplyPS, userPS);
        boardReplyLikeRepository.save(boardReplyLike);

        // 5. 좋아요 수 조회
        Integer boardLikeCount = boardReplyLikeRepository.findTotalCount(replyId);

        BoardReplyLikeResponse.SaveDTO respDTO = new BoardReplyLikeResponse.SaveDTO(boardReplyLike, boardLikeCount);

        // 5. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }

    @Transactional
    public Object delete(Integer likeId, User sessionUser) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 2. board/reply/like 조회
        BoardReplyLike boardReplyLikePS = boardReplyLikeRepository.findById(likeId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 3. 주인인지 확인
        if (boardReplyLikePS.getUser().getId() != userPS.getId()) {
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 좋아요 삭제
        boardReplyLikeRepository.deleteById(boardReplyLikePS.getId());

        // 5. 좋아요 갯수 반환
        Integer boardLikeCount = boardReplyLikeRepository.findTotalCount(boardReplyLikePS.getId());

        // 6. DTO에 옮기기
        BoardLikeResponse.DeleteDTO respDTO = new BoardLikeResponse.DeleteDTO(boardLikeCount);

        return respDTO;
    }
}
