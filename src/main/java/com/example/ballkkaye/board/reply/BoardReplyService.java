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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

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

        BoardReplyResponse.SaveDTO respDTO = new BoardReplyResponse.SaveDTO(boardReply, boardPS, userPS, relativeTime);
        return respDTO;
    }

    // 댓글 삭제
    @Transactional
    public Object delete(Integer replyId, User sessionUser) {
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
        BoardReplyResponse.DeleteDTO respDTO = new BoardReplyResponse.DeleteDTO();
        return respDTO;
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

    public Object detail(Integer boardId, User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        PrettyTime p = new PrettyTime(Locale.KOREAN);

        List<BoardReply> parentReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, null);
        List<BoardReplyResponse.ParentItemDTO> result = new ArrayList<>();

        for (BoardReply parent : parentReplies) {
            String parentRelativeTime = p.format(new Date(parent.getCreatedAt().getTime()));

            List<BoardReply> childReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, parent.getId());

            List<BoardReplyResponse.ChildItemDTO> childDTOs = childReplies.stream().map(child ->
                    new BoardReplyResponse.ChildItemDTO(
                            child.getId(),
                            child.getUser().getNickname(),
                            child.getUser().getProfileUrl(),
                            p.format(new Date(child.getCreatedAt().getTime())),
                            child.getUser().getTeam().getTeamName(),
                            child.getContent(),
                            child.getParentReplyId().getId(),
                            child.getTagReplyId() != null ? child.getTagReplyId().getId() : null,
                            child.getTagReplyId() != null ? child.getTagReplyId().getUser().getNickname() : null,
                            sessionUser.getId().equals(child.getUser().getId()),
                            boardReplyLikeRepository.findByReplyIdAndUserId(child.getId(), sessionUser.getId()).isPresent(),
                            boardReplyLikeRepository.findTotalCount(child.getId())
                    )
            ).toList();


            String parentTeamName = parent.getUser().getTeam().getTeamName();
            Integer parentTagReplyId = parent.getTagReplyId() != null ? parent.getTagReplyId().getId() : null;

            BoardReplyResponse.ParentItemDTO parentDTO = new BoardReplyResponse.ParentItemDTO(
                    parent.getId(),
                    parent.getUser().getNickname(),
                    parent.getUser().getProfileUrl(),
                    parentRelativeTime,
                    null,
                    parentTagReplyId,
                    parentTeamName,
                    parent.getContent(),
                    sessionUser.getId().equals(parent.getUser().getId()),
                    boardReplyLikeRepository.findByReplyIdAndUserId(parent.getId(), sessionUser.getId()).isPresent(),
                    boardReplyLikeRepository.findTotalCount(parent.getId()),
                    childDTOs
            );

            result.add(parentDTO);
        }

        return result;
    }
}
