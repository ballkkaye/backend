package com.example.ballkkaye.board.reply.like;

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
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 게시글이 존재하는지 확인
        BoardReply boardReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 이미 존재하는 좋아요인지 검색
        if (boardReplyLikeRepository.findByReplyIdAndUserId(replyId, userPS.getId()).isPresent()) {
            throw new RuntimeException("이미 좋아요 한 게시물입니다.");
        }

        // 4. 없으면 저장
        BoardReplyLike boardReplyLike = new BoardReplyLike(boardReplyPS, userPS);
        boardReplyLikeRepository.save(boardReplyLike);

        // 5. 좋아요 수 조회
        Integer boardLikeCount = boardReplyLikeRepository.findByReplyId(replyId);

        BoardReplyLikeResponse.SaveDTO respDTO = new BoardReplyLikeResponse.SaveDTO(boardReplyLike, boardLikeCount);

        // 5. 저장된 객체 반환 (PK 포함됨)
        return respDTO;
    }
}
