package com.example.ballkkaye.board.reply;

import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.user.User;
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
        if (parentReply.getParentReplyId() != null) {
            throw new RuntimeException("잘못된 접근");
        }

        BoardReply boardReply = new BoardReply(boardPS, sessionUser, parentReply, tagReply, DeleteStatus.NOT_DELETED, reqDTO.getContent());

        boardReplyRepository.save(boardReply);
        String relativeTime = p.format(new Date(boardReply.getCreatedAt().getTime()));

        BoardReplyResponse.SaveDTO respDTO = new BoardReplyResponse.SaveDTO(boardReply, boardPS, parentReply, tagReply, sessionUser, relativeTime);
        return respDTO;
    }
}
