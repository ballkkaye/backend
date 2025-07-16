package com.example.ballkkaye.board;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.board.image.BoardImage;
import com.example.ballkkaye.board.image.BoardImageRepository;
import com.example.ballkkaye.board.image.BoardImageResponse;
import com.example.ballkkaye.board.like.BoardLikeRepository;
import com.example.ballkkaye.board.reply.BoardReply;
import com.example.ballkkaye.board.reply.BoardReplyRepository;
import com.example.ballkkaye.board.reply.BoardReplyResponse;
import com.example.ballkkaye.board.reply.like.BoardReplyLike;
import com.example.ballkkaye.board.reply.like.BoardReplyLikeRepository;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.team.TeamResponse;
import com.example.ballkkaye.team.record.TeamRecordRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final BoardReplyLikeRepository boardReplyLikeRepository;
    private final BoardLikeRepository boardLikeRepository;

    // 커뮤니티 게시글 등록
    @Transactional
    public BoardResponse.SaveDTO save(BoardRequest.SaveDTO reqDTO, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);

        log.info("[게시글 등록 요청] userId={}, teamId={}, 이미지 수={}",
                sessionUser.getId(),
                reqDTO.getTeamId(),
                reqDTO.getImages() != null ? reqDTO.getImages().size() : 0
        );


        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("게시글 등록 실패 - 존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 2. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("게시글 등록 실패 - 존재하지 않는 팀: teamId={}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 이미지 최대 수 검사
        List<String> base64Images = reqDTO.getImages();
        if (base64Images != null && base64Images.size() > 10) {
            log.warn("게시글 등록 실패 - 이미지 수 초과: userId={}, count={}", sessionUser.getId(), base64Images.size());
            throw new ExceptionApi400("최대 이미지 저장 한도를 넘었습니다.");
        }

        // 4. 게시글 저장
        Board board = reqDTO.toEntity(userPS, teamPS);
        boardRepository.save(board);
        log.info("게시글 저장 완료: boardId={}, userId={}", board.getId(), sessionUser.getId());


        // 5. 이미지 저장
        List<BoardImageResponse.ItemDTO> itemDTOS = new ArrayList<>();
        if (base64Images != null) {
            for (String img : base64Images) {
                BoardImage boardImage = new BoardImage()
                        .builder()
                        .board(board)
                        .deleteStatus(DeleteStatus.NOT_DELETED)
                        .imgUrl(img)
                        .build();
                boardImageRepository.save(boardImage);
                itemDTOS.add(new BoardImageResponse.ItemDTO(boardImage.getId(), boardImage.getImgUrl()));
            }
            log.debug("이미지 {}건 저장 완료: boardId={}", base64Images.size(), board.getId());
        }


        // 6. 응답 반환
        return new BoardResponse.SaveDTO(board, itemDTOS, p.format(new Date(board.getCreatedAt().getTime())));
    }

    // 커뮤니티 게시글 수정
    @Transactional
    public BoardResponse.UpdateDTO update(BoardRequest.UpdateDTO reqDTO, User sessionUser, Integer boardId) {
        log.info("[게시글 수정 요청] boardId={}, userId={}, 새 이미지 수={}, 기존 유지 이미지 수={}",
                boardId, sessionUser.getId(),
                reqDTO.getNewImages().size(),
                reqDTO.getRemainImageUrls().size());

        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> {
                    log.warn("게시글 수정 실패 - 존재하지 않는 사용자: userId={}", sessionUser.getId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });


        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.warn("게시글 수정 실패 - 존재하지 않는 게시글: boardId={}", boardId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 3. 게시글의 소유자인지 확인
        if (!boardPS.getUser().getId().equals(userPS.getId())) {
            log.warn("게시글 수정 실패 - 권한 없음: userId={}, boardOwnerId={}", sessionUser.getId(), boardPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 존재하는 팀인지 확인
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> {
                    log.warn("게시글 수정 실패 - 존재하지 않는 팀: teamId={}", reqDTO.getTeamId());
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 5. 이미지 수 10개 넘으면 throw
        int totalImageCount = reqDTO.getNewImages().size() + reqDTO.getRemainImageUrls().size();
        if (reqDTO.getNewImages().size() + reqDTO.getRemainImageUrls().size() > 10) {
            log.warn("게시글 수정 실패 - 이미지 수 초과: userId={}, count={}", sessionUser.getId(), totalImageCount);
            throw new ExceptionApi400("최대 이미지 저장 한도를 넘었습니다.");
        }

        // 5. 기존 이미지 조회
        List<BoardImage> images = boardImageRepository.findByBoardIdAndDeleteStatus(boardPS, DeleteStatus.NOT_DELETED);

        // 6. 삭제할 이미지 찾아서 삭제 호출 << 컬럼 상태값 갱신
        int deletedCount = 0;
        for (BoardImage image : images) {
            if (!reqDTO.getRemainImageUrls().contains(image.getImgUrl())) {
                image.delete();
            }
        }

        // 7. 새 이미지 저장
        int addedCount = 0;
        for (String img : reqDTO.getNewImages()) {
            BoardImage boardImage = new BoardImage()
                    .builder()
                    .board(boardPS)
                    .deleteStatus(DeleteStatus.NOT_DELETED)
                    .imgUrl(img)
                    .build();
            boardImageRepository.save(boardImage);
        }
        log.info("게시글 이미지 수정 완료: deleted={}, added={}", deletedCount, addedCount);

        // 8. 게시글 내용 수정

        // 8. 게시글 update 성공
        boardPS.update(reqDTO.getTitle(), reqDTO.getContent(), teamPS);
        log.info("게시글 내용 수정 완료: boardId={}, title={}", boardPS.getId(), reqDTO.getTitle());


        // 9. udpate한 게시글 이미지들 조회
        List<BoardImage> newImagesUrl = boardImageRepository.findByBoardIdAndDeleteStatus(boardPS, DeleteStatus.NOT_DELETED);
        List<BoardImageResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (BoardImage image : newImagesUrl) {
            itemDTOS.add(new BoardImageResponse.ItemDTO(image.getId(), image.getImgUrl()));
        }

        // 갱신된 게시글 + 이미지 반환
        return new BoardResponse.UpdateDTO(boardPS, itemDTOS);
    }

    // 게시글 목록 조회
    public BoardResponse.ListDTO getList(Integer teamId, Integer page) {
        log.info("[게시글 목록 조회 요청] teamId={}, page={}", teamId, page);

        PrettyTime p = new PrettyTime(Locale.KOREAN);
        List<Board> boards = boardRepository.findAll(teamId, page, DeleteStatus.NOT_DELETED);
        List<BoardResponse.ItemDTO> itemDTOS = new ArrayList<>();
        log.info("게시글 조회 결과: {}건", boards.size());

        for (Board board : boards) {
            String title = board.getTitle();
            String nickname = board.getUser().getNickname();
            String relativeTime = p.format(new Date(board.getCreatedAt().getTime()));
            Integer tId = board.getTeam() != null ? board.getTeam().getId() : null;
            String teamName = board.getTeam() != null ? board.getTeam().getTeamName() : null;
            Integer replyCount = boardReplyRepository.totalCount(board.getId());
            Integer likeCount = boardLikeRepository.totalCount(board.getId());


            BoardResponse.ItemDTO dto = new BoardResponse.ItemDTO(
                    board.getId(),
                    title,
                    nickname,
                    relativeTime,
                    tId,
                    teamName,
                    likeCount,
                    replyCount
            );

            itemDTOS.add(dto);
        }
        List<TeamResponse.ItemDTO> teamDTOS = new ArrayList<>();
        List<Team> teams = teamRepository.findAll();
        log.info("팀 리스트 조회 결과: {}개 팀", teams.size());
        for (Team team : teams) {

            TeamResponse.ItemDTO dto = new TeamResponse.ItemDTO(
                    team.getId(),
                    team.getTeamName(),
                    team.getLogoUrl(),
                    teamRecordRepository.getRank(team.getId())
            );
            teamDTOS.add(dto);
        }
        BoardResponse.ListDTO respDTO = new BoardResponse.ListDTO(teamDTOS, itemDTOS);
        log.info("게시글 목록 응답 완료: 게시글={}건, 팀={}개", itemDTOS.size(), teamDTOS.size());
        return respDTO;
    }

    // 게시글 상세보기
    public BoardResponse.DetailWithReplyDTO getDetailWithReply(Integer boardId, User sessionUser) {
        log.info("[게시글 상세보기 요청] boardId={}, 요청자 userId={}", boardId, sessionUser.getId());

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        PrettyTime p = new PrettyTime(Locale.KOREAN);

        List<BoardReply> parentsReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, null);
        List<BoardReplyResponse.ParentItemDTO> parentReplyItemDTOs = new ArrayList<>();
        Boolean isParentReplyOwner = false;
        Boolean isParentReplyLike = false;
        Boolean isChildReplyOwner = false;
        Boolean isChildReplyLike = false;

        for (BoardReply parentReply : parentsReplies) {
            List<BoardReplyResponse.ChildItemDTO> childReplyItemDTOs = new ArrayList<>();
            Integer replyId = parentReply.getId();
            String nickname = parentReply.getUser().getNickname();
            String profileImg = parentReply.getUser().getProfileUrl();
            String relativeTime = p.format(new Date(board.getCreatedAt().getTime()));
            String myTeamName = parentReply.getUser().getTeam() == null ? null : parentReply.getUser().getTeam().getTeamName();
            String content = parentReply.getContent();
            Integer parentReplyId = parentReply.getParentReplyId() != null
                    ? parentReply.getParentReplyId().getId()
                    : null;
            Integer parentTagReplyId = null;
            if (parentReply.getUser().getId() == sessionUser.getId()) {
                isParentReplyOwner = true;
            }
            Optional<BoardReplyLike> parentReplyLikeOP = boardReplyLikeRepository.findByReplyIdAndUserId(replyId, sessionUser.getId());
            if (parentReplyLikeOP.isPresent()) {
                isParentReplyLike = true;
            }

            List<BoardReply> childrenReplies = boardReplyRepository.findByBoardIdAndParentReplyId(boardId, replyId);
            for (BoardReply childReply : childrenReplies) {
                Integer childReplyId = childReply.getId();
                String childNickname = childReply.getUser().getNickname();
                String childProfileImg = childReply.getUser().getProfileUrl();
                String childRelativeTime = p.format(new Date(childReply.getCreatedAt().getTime()));
                String childMyTeamName = childReply.getUser().getTeam() == null ? null : childReply.getUser().getTeam().getTeamName();
                String childContent = childReply.getContent();
                Integer childParentReplyId = childReply.getParentReplyId() != null
                        ? childReply.getParentReplyId().getId()
                        : null;

                Integer childParentTagReplyId = childReply.getTagReplyId() != null
                        ? childReply.getTagReplyId().getId()
                        : null;

                String childParentTagReplyName = childReply.getTagReplyId() != null
                        ? childReply.getTagReplyId().getUser().getNickname()
                        : null;

                if (childReply.getUser().getId() == sessionUser.getId()) {
                    isChildReplyOwner = true;
                }
                Optional<BoardReplyLike> childReplyLikeOP = boardReplyLikeRepository.findByReplyIdAndUserId(replyId, sessionUser.getId());
                if (childReplyLikeOP.isPresent()) {
                    isChildReplyLike = true;
                }
                Integer likeCount = boardReplyLikeRepository.findTotalCount(replyId);
                BoardReplyResponse.ChildItemDTO dto = new BoardReplyResponse.ChildItemDTO(
                        childReplyId,
                        childNickname,
                        childProfileImg,
                        childRelativeTime,
                        childMyTeamName,
                        childContent,
                        childParentReplyId,
                        childParentTagReplyId,
                        childParentTagReplyName,
                        isChildReplyOwner,
                        isChildReplyLike,
                        likeCount

                );
                childReplyItemDTOs.add(dto);
            }
            Integer likeCount = boardReplyLikeRepository.findTotalCount(replyId);

            BoardReplyResponse.ParentItemDTO dto = new BoardReplyResponse.ParentItemDTO(
                    replyId,
                    nickname,
                    profileImg,
                    relativeTime,
                    parentReplyId,
                    parentTagReplyId,
                    myTeamName,
                    content,
                    isParentReplyOwner,
                    isParentReplyLike,
                    likeCount,
                    childReplyItemDTOs
            );
            parentReplyItemDTOs.add(dto);
        }
        Boolean isBoardOwner = board.getUser().getId() == sessionUser.getId();
        Boolean isBoardLike = boardLikeRepository.findByBoardIdAndUserId(board.getId(), sessionUser.getId())
                .isPresent();
        Integer replyCount = boardLikeRepository.totalCount(board.getId());

        // 2. 이미지 조회
        List<BoardImage> images = boardImageRepository.findByBoardIdAndDeleteStatus(board, DeleteStatus.NOT_DELETED);

        List<BoardImageResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (BoardImage image : images) {
            itemDTOS.add(new BoardImageResponse.ItemDTO(image.getId(), image.getImgUrl()));
        }


        BoardResponse.DetailWithReplyDTO respDTO = new BoardResponse.DetailWithReplyDTO(
                board.getId(),
                board.getUser().getNickname(),
                board.getUser().getProfileUrl(),
                p.format(new Date(board.getCreatedAt().getTime())),
                board.getUser().getTeam().getTeamName(),
                board.getTeam().getId(),
                board.getTeam().getTeamName(),
                board.getTitle(),
                board.getContent(),
                isBoardOwner,
                isBoardLike,
                replyCount,
                itemDTOS,
                parentReplyItemDTOs);

        log.info("게시글 ID={} 상세 조회 완료 - 댓글 수={}, 이미지 수={}",
                boardId, parentReplyItemDTOs.size(), itemDTOS.size());

        return respDTO;
    }


    // 게시글 삭제
    @Transactional
    public Object delete(Integer boardId, User sessionUser) {
        log.info("[게시글 삭제 요청] userId={}, boardId={}", sessionUser.getId(), boardId);

        // 1. 존재하는 유저인지
        userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));

        // 3. 게시글 주인이 맞는지
        if (boardPS.getUser().getId() != sessionUser.getId()) {
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 4. 게시글 DeleteStatus 상태 변경
        boardPS.delete();
        log.info("게시글 삭제 완료 - boardId={}, 삭제한 사용자={}", boardId, sessionUser.getId());

        // 5. ok
        return new BoardResponse.DeleteDTO();
    }

    public Object getDetail(Integer boardId, User sessionUser) {
        log.info("[게시글 상세조회] userId={}, boardId={}", sessionUser.getId(), boardId);

        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        Boolean isOwner = false;
        Boolean isLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userPS.getId()).isPresent();
        Integer likeCount = boardLikeRepository.totalCount(boardId);
        // 3. 게시글 주인이 맞는지
        if (boardPS.getUser().getId() == userPS.getId()) {
            isOwner = true;
        }
        List<BoardImage> boardImages = boardImageRepository.findByBoardIdAndDeleteStatus(boardPS, DeleteStatus.NOT_DELETED);
        List<BoardImageResponse.ItemDTO> itemDTOS = new ArrayList<>();
        for (BoardImage image : boardImages) {
            itemDTOS.add(new BoardImageResponse.ItemDTO(image.getId(), image.getImgUrl()));
        }

        log.info("상세조회 결과 - isOwner={}, isLike={}, likeCount={}, imageCount={}",
                isOwner, isLike, likeCount, itemDTOS.size());

        PrettyTime p = new PrettyTime(Locale.KOREAN);
        String relativeTime = p.format(new Date(boardPS.getCreatedAt().getTime()));
        BoardResponse.DetailDTO respDTO = new BoardResponse.DetailDTO(boardPS, userPS, relativeTime, isOwner, isLike, likeCount, itemDTOS);
        return respDTO;
    }
}