package com.example.ballkkaye.board.reply;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.board.reply.like.BoardReplyLikeRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardReplyLikeRepository boardReplyLikeRepository;

    // 댓글 등록 
    @Transactional
    public Object save(Integer boardId, User sessionUser, BoardReplyRequest.SaveDTO reqDTO) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        BoardReply parentReply = null;
        BoardReply tagReply = null;
        if (reqDTO.getParentReplyId() != null) {
            parentReply = boardReplyRepository.findById(reqDTO.getParentReplyId())
                    .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        }

        if (reqDTO.getTagReplyId() != null) {
            tagReply = boardReplyRepository.findById(reqDTO.getTagReplyId())
                    .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        }
        if (parentReply != null && parentReply.getParentReplyId() != null) {
            throw new RuntimeException("잘못된 접근");
        }

        BoardReply boardReply = new BoardReply(boardPS, sessionUser, parentReply, tagReply, DeleteStatus.NOT_DELETED, reqDTO.getContent());

        boardReplyRepository.save(boardReply);
        String relativeTime = p.format(new Date(boardReply.getCreatedAt().getTime()));

        BoardReplyResponse.SaveDTO respDTO = new BoardReplyResponse.SaveDTO(boardReply, boardPS, sessionUser, relativeTime);
        return respDTO;
    }

    // 댓글 삭제
    @Transactional
    public void delete(Integer replyId, User sessionUser) {
        // 1. 존재하는 유저인지
        userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 존재하는 댓글인지 확인
        BoardReply boardReply = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 주인인지 확인
        if (boardReply.getUser().getId() != sessionUser.getId()) {
            throw new RuntimeException("해당 기능에 대한 권한이 없습니다.");
        }

        // 4. 삭제
        boardReply.delete();

    }

    @Transactional
    public Object update(BoardReplyRequest.UpdateDTO reqDTO, Integer replyId, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);

        BoardReply boardReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 3. 주인인지 확인
        if (boardReplyPS.getUser().getId() != sessionUser.getId()) {
            throw new RuntimeException("해당 기능에 대한 권한이 없습니다.");
        }

        // 존재하는 댓글인지 조회(tagReplyId)
        BoardReply tagReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 4. 업데이트
        boardReplyPS.update(reqDTO.getContent(), tagReplyPS);
        String relativeTime = p.format(new Date(boardReplyPS.getCreatedAt().getTime()));

        Integer replyLikeCount = boardReplyLikeRepository.findTotalCount(replyId);
        Boolean isLike = boardReplyLikeRepository.findByReplyIdAndUserId(replyId, sessionUser.getId()).isPresent();


        BoardReplyResponse.UpdateDTO respDTO = new BoardReplyResponse.UpdateDTO(boardReplyPS, relativeTime, replyLikeCount, isLike);
        return respDTO;
    }
}
