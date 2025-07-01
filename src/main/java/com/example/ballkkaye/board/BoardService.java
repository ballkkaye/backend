package com.example.ballkkaye.board;

import com.example.ballkkaye._core.util.ImageUtil;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.util.*;

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
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 팀 조회
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));

        // 3. 이미지 최대 수 검사
        List<String> base64Images = reqDTO.getImages();
        if (base64Images != null && base64Images.size() > 10) {
            throw new RuntimeException("최대 이미지 저장 한도를 넘었습니다.");
        }

        // 4. 게시글 저장
        Board board = reqDTO.toEntity(userPS, teamPS);
        boardRepository.save(board);

        // 5. 이미지 저장
        List<BoardImageResponse.ItemDTO> imageUrls = ImageUtil.saveBase64Images(
                base64Images,
                board,
                boardImageRepository
        );

        // 6. 응답 반환
        return new BoardResponse.SaveDTO(board, imageUrls);
    }

    // 커뮤니티 게시글 수정
    @Transactional
    public BoardResponse.UpdateDTO update(BoardRequest.@Valid UpdateDTO reqDTO, User sessionUser, Integer boardId) {
        // 1. user 조회
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        // 3. 게시글의 소유자인지 확인
        if (!boardPS.getUser().getId().equals(userPS.getId())) throw new RuntimeException("403 예외처리 예정");

        // 4. 존재하는 팀인지 확인
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));

        // 5. 이미지 수 10개 넘으면 throw
        if (reqDTO.getNewImages().size() + reqDTO.getRemainImageUrls().size() > 10) {
            throw new RuntimeException("이미지 너무 많음");
        }

        // 5. 기존 이미지 조회
        List<BoardImage> images = boardImageRepository.findByBoardIdAndDeleteStatus(boardPS, DeleteStatus.NOT_DELETED);

        // 6. 삭제할 이미지 찾아서 삭제 호출 << 컬럼 상태값 갱신
        for (BoardImage image : images) {
            if (!reqDTO.getRemainImageUrls().contains(image.getImgUrl())) {
                image.delete();
            }
        }

        // 7. 새 이미지 저장
        ImageUtil.saveBase64Images(reqDTO.getNewImages(), boardPS, boardImageRepository);

        // 8. 게시글 update 성공
        boardPS.update(reqDTO.getTitle(), reqDTO.getContent(), teamPS);

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
    public BoardResponse.ListDTO getBoards(Integer teamId, Integer page) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        List<Board> boards = boardRepository.findAll(teamId, page, DeleteStatus.NOT_DELETED);
        List<BoardResponse.ItemDTO> itemDTOS = new ArrayList<>();

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
        return respDTO;
    }

    // 게시글 상세보기
    public BoardResponse.DetailDTO detail(Integer boardId, User sessionUser) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));
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
            String myTeamName = parentReply.getUser().getTeam().getTeamName();
            String content = parentReply.getContent();
            Integer parentReplyId = parentReply.getParentReplyId().getId();
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
                String childMyTeamName = childReply.getUser().getTeam().getTeamName();
                String childContent = childReply.getContent();
                Integer childParentReplyId = childReply.getParentReplyId().getId();
                Integer childParentTagReplyId = childReply.getTagReplyId().getId();
                String childParentTagReplyName = childReply.getTagReplyId().getUser().getNickname();
                if (childReply.getUser().getId() == sessionUser.getId()) {
                    isChildReplyOwner = true;
                }
                Optional<BoardReplyLike> childReplyLikeOP = boardReplyLikeRepository.findByReplyIdAndUserId(replyId, sessionUser.getId());
                if (childReplyLikeOP.isPresent()) {
                    isChildReplyLike = true;
                }
                Integer likeCount = boardReplyLikeRepository.findByReplyId(replyId);
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
            Integer likeCount = boardReplyLikeRepository.findByReplyId(replyId);

            BoardReplyResponse.ParentItemDTO dto = new BoardReplyResponse.ParentItemDTO(
                    replyId,
                    nickname,
                    profileImg,
                    relativeTime,
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


        BoardResponse.DetailDTO respDTO = new BoardResponse.DetailDTO(
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
                parentReplyItemDTOs);

        return respDTO;
    }


    // 게시글 삭제
    @Transactional
    public void delete(Integer boardId, User sessionUser) {
        // 1. 존재하는 유저인지
        userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 2. 게시글이 존재하는지 확인
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        // 3. 게시글 주인이 맞는지
        if (boardPS.getUser().getId() != sessionUser.getId()) {
            throw new RuntimeException("403 예외처리 예정");
        }

        // 4. 게시글 DeleteStatus 상태 변경
        boardPS.delete();

        // 5. ok
    }
}
