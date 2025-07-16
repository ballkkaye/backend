package com.example.ballkkaye.board.reply;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.board.Board;
import com.example.ballkkaye.board.BoardRepository;
import com.example.ballkkaye.board.reply.like.BoardReplyLikeRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardReplyLikeRepository boardReplyLikeRepository;

    @Transactional
    public Object save(Integer boardId, User sessionUser, BoardReplyRequest.SaveDTO reqDTO) {
        log.info("[댓글 작성 요청] boardId={}, userId={}, parentReplyId={}, tagReplyId={}",
                boardId, sessionUser.getId(), reqDTO.getParentReplyId(), reqDTO.getTagReplyId());

        PrettyTime p = new PrettyTime(Locale.KOREAN);
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 게시글: boardId={}", boardId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });
        BoardReply parentReply = null;
        BoardReply tagReply = null;
        if (reqDTO.getParentReplyId() != null) {
            parentReply = boardReplyRepository.findById(reqDTO.getParentReplyId())
                    .orElseThrow(() -> {
                        log.warn("존재하지 않는 부모 댓글: parentReplyId={}", reqDTO.getParentReplyId());
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
        }

        if (reqDTO.getTagReplyId() != null) {
            tagReply = boardReplyRepository.findById(reqDTO.getTagReplyId())
                    .orElseThrow(() -> {
                        log.warn("존재하지 않는 태그 댓글: tagReplyId={}", reqDTO.getTagReplyId());
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
        }
        if (parentReply != null && parentReply.getParentReplyId() != null) {
            log.warn("대댓글에 다시 대댓글 달려고 시도함: parentReplyId={}", parentReply.getId());
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        BoardReply boardReply = new BoardReply(boardPS, sessionUser, parentReply, tagReply, DeleteStatus.NOT_DELETED, reqDTO.getContent());

        boardReplyRepository.save(boardReply);
        String relativeTime = p.format(new Date(boardReply.getCreatedAt().getTime()));

        BoardReplyResponse.SaveDTO respDTO = new BoardReplyResponse.SaveDTO(boardReply, boardPS, userPS, relativeTime);
        log.info("[댓글 저장 완료] replyId={}, boardId={}, userId={}", boardReply.getId(), boardId, userPS.getId());
        return respDTO;
    }

    // 댓글 삭제
    @Transactional
    public Object delete(Integer replyId, User sessionUser) {
        log.info("[댓글 삭제 요청] replyId={}, userId={}", replyId, sessionUser.getId());


        // 1. 존재하는 유저인지
        userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. 존재하는 댓글인지 확인
        BoardReply boardReply = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 댓글: replyId={}", replyId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 주인인지 확인
        if (boardReply.getUser().getId() != sessionUser.getId()) {
            log.warn("댓글 삭제 권한 없음: replyId={}, 요청자={}, 실제작성자={}",
                    replyId, sessionUser.getId(), boardReply.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 삭제
        boardReply.delete();
        log.info("[댓글 삭제 완료] replyId={}, userId={}", replyId, sessionUser.getId());

        BoardReplyResponse.DeleteDTO respDTO = new BoardReplyResponse.DeleteDTO();
        return respDTO;
    }

    @Transactional
    public Object update(BoardReplyRequest.UpdateDTO reqDTO, Integer replyId, User sessionUser) {
        log.info("[댓글 수정 요청] replyId={}, userId={}", replyId, sessionUser.getId());

        PrettyTime p = new PrettyTime(Locale.KOREAN);

        BoardReply boardReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    log.warn("댓글 없음: replyId={}", replyId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 주인인지 확인
        if (boardReplyPS.getUser().getId() != sessionUser.getId()) {
            log.warn("댓글 수정 권한 없음: replyId={}, 요청자={}, 실제작성자={}",
                    replyId, sessionUser.getId(), boardReplyPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 존재하는 댓글인지 조회(tagReplyId)
        BoardReply tagReplyPS = boardReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    log.warn("Tag 대상 댓글 없음: replyId={}", replyId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 4. 업데이트
        boardReplyPS.update(reqDTO.getContent(), tagReplyPS);
        log.info("[댓글 수정 완료] replyId={}, userId={}", replyId, sessionUser.getId());
        String relativeTime = p.format(new Date(boardReplyPS.getCreatedAt().getTime()));

        Integer replyLikeCount = boardReplyLikeRepository.findTotalCount(replyId);
        Boolean isLike = boardReplyLikeRepository.findByReplyIdAndUserId(replyId, sessionUser.getId()).isPresent();


        BoardReplyResponse.UpdateDTO respDTO = new BoardReplyResponse.UpdateDTO(boardReplyPS, relativeTime, replyLikeCount, isLike);
        return respDTO;
    }

    public Object getDetail(Integer boardId, User sessionUser) {
        log.info("[댓글 상세 조회 요청] boardId={}, userId={}", boardId, sessionUser.getId());

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("댓글 조회 실패 - 존재하지 않는 유저: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.warn("댓글 조회 실패 - 존재하지 않는 게시글: boardId={}", boardId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        PrettyTime p = new PrettyTime(Locale.KOREAN);

        List<BoardReply> parentReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, null);
        log.info("부모 댓글 개수: {}", parentReplies.size());

        List<BoardReplyResponse.ParentItemDTO> result = new ArrayList<>();

        for (BoardReply parent : parentReplies) {
            String parentRelativeTime = p.format(new Date(parent.getCreatedAt().getTime()));

            List<BoardReply> childReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, parent.getId());
            log.debug("자식 댓글 개수 (parentReplyId={}): {}", parent.getId(), childReplies.size());

            List<BoardReplyResponse.ChildItemDTO> childDTOs = childReplies.stream().map(child ->
                    new BoardReplyResponse.ChildItemDTO(
                            child.getId(),
                            child.getUser().getNickname(),
                            child.getUser().getProfileUrl(),
                            p.format(new Date(child.getCreatedAt().getTime())),
                            child.getUser().getTeam() != null ? child.getUser().getTeam().getTeamName() : null,
                            child.getContent(),
                            child.getParentReplyId().getId(),
                            child.getTagReplyId() != null ? child.getTagReplyId().getId() : null,
                            child.getTagReplyId() != null ? child.getTagReplyId().getUser().getNickname() : null,
                            sessionUser.getId().equals(child.getUser().getId()),
                            boardReplyLikeRepository.findByReplyIdAndUserId(child.getId(), sessionUser.getId()).isPresent(),
                            boardReplyLikeRepository.findTotalCount(child.getId())
                    )
            ).toList();


            String parentTeamName = parent.getUser().getTeam() != null
                    ? parent.getUser().getTeam().getTeamName()
                    : null;
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

        log.info("댓글 상세 조회 완료 - 총 댓글 트리 수: {}", result.size());

        return result;
    }
}
