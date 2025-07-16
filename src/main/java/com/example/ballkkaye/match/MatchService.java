package com.example.ballkkaye.match;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import com.example.ballkkaye._core.error.ex.ExceptionApi403;
import com.example.ballkkaye._core.error.ex.ExceptionApi404;
import com.example.ballkkaye.common.enums.Age;
import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.common.enums.Gender;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.ChatRoomResponse;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUser;
import com.example.ballkkaye.match.chat.room.user.ChatRoomUserRepository;
import com.example.ballkkaye.match.like.MatchLikeRepository;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MatchLikeRepository matchLikeRepository;

    // 동행글 작성
    @Transactional
    public Object save(User sessionUser, MatchRequest.SaveDTO reqDTO) {
        log.info("동행글 작성 요청 - userId: {}, gameId: {}, teamId: {}", sessionUser.getId(), reqDTO.getGameId(), reqDTO.getTeamId());
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new ExceptionApi404("해당 자원을 찾을 수 없습니다."));
        if (gamePS.getGameTime().before(new Timestamp(System.currentTimeMillis()))) {
            log.warn("과거 경기로 동행글 작성 시도 - gameId: {}", gamePS.getId());
            throw new ExceptionApi400("잘못된 요청입니다.");
        }
        if (gamePS.getAwayTeam().getId() != reqDTO.getTeamId() && gamePS.getHomeTeam().getId() != reqDTO.getTeamId()) {
            log.warn("경기와 무관한 팀으로 동행글 작성 시도 - gameId: {}, 요청 teamId: {}", gamePS.getId(), reqDTO.getTeamId());
            throw new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
        }

        ChatRoom chatRoom = new ChatRoom().builder()
                .game(gamePS)
                .team(teamPS)
                .maxParticipants(reqDTO.getMaxParticipants())
                .preferredGender(reqDTO.getPreferredGender())
                .preferredAge(reqDTO.getPreferredAge())
                .deleteStatus(DeleteStatus.NOT_DELETED)
                .isSameTeam(reqDTO.getIsSameTeam())
                .build();
        chatRoomRepository.save(chatRoom);
        log.info("채팅방 생성 완료 - chatRoomId: {}", chatRoom.getId());


        var chatReeomUser = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoom.getId());
        if (chatReeomUser.isPresent()) {
            log.warn("사용자가 이미 채팅방에 참여중 - userId: {}, chatRoomId: {}", userPS.getId(), chatRoom.getId());
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        chatRoomUserRepository.save(
                new ChatRoomUser()
                        .builder()
                        .chatRoom(chatRoom)
                        .user(userPS)
                        .isOwner(true)
                        .deleteStatus(DeleteStatus.NOT_DELETED)
                        .build());
        log.info("채팅방 유저 등록 완료 - userId: {}, chatRoomId: {}", userPS.getId(), chatRoom.getId());


        Match match = new Match().builder()
                .user(userPS)
                .chatRoom(chatRoom)
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .build();
        matchRepository.save(match);
        log.info("동행글 저장 완료 - matchId: {}", match.getId());

        ChatRoomResponse.DTO chatRoomDTO = new ChatRoomResponse.DTO(chatRoom);
        MatchResponse.DTO matchDTO = new MatchResponse.DTO(match);
        MatchResponse.SaveDTO respDTO = new MatchResponse.SaveDTO(chatRoomDTO, matchDTO);

        return respDTO;
    }

    // 동행글 목록 조회
    public Object getMatches(User sessionUser, Integer page, Gender gender, Age age, Integer teamId) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        String selectedTimeName = null;
        Gender selectedGender = gender == null ? Gender.NONE : gender;
        Age selectedAge = age == null ? Age.NONE : age;

        log.info("동행글 목록 조회 요청 - userId: {}, page: {}, gender: {}, age: {}, teamId: {}",
                sessionUser.getId(), page, selectedGender, selectedAge, teamId);

        if (teamId != null) {
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> {
                        log.warn("존재하지 않는 팀 ID로 요청됨 - teamId: {}", teamId);
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
            selectedTimeName = team.getTeamName();
        }
        List<Match> matches = matchRepository.findAll(page, selectedGender, selectedAge, teamId);
        log.info("조회된 동행글 수: {}", matches.size());

        List<MatchResponse.Item> items = new ArrayList<>();
        for (Match match : matches) {
            String countUser = chatRoomUserRepository.countByChatRoomId(match.getId()).toString();
            String relativeTime = p.format(new Date(match.getCreatedAt().getTime()));
            MatchResponse.Item item = new MatchResponse.Item(
                    match, countUser, relativeTime
            );
            items.add(item);
        }

        MatchResponse.ListDTO respDTO = new MatchResponse.ListDTO(selectedGender, selectedAge, teamId, selectedTimeName, items);
        return respDTO;
    }

    // 매칭글 상세보기
    public Object getDetail(Integer matchId, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);

        log.info("매칭글 상세조회 요청 - matchId: {}, userId: {}", matchId, sessionUser.getId());


        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 매칭글 요청 - matchId: {}", matchId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        String relativeTime = p.format(new Date(matchPS.getCreatedAt().getTime()));
        Boolean isOwner = false;
        if (matchPS.getUser().getId().equals(sessionUser.getId())) {
            isOwner = true;
        }
        Integer likeCount = matchLikeRepository.totalCount(matchId);
        Boolean isLike = matchLikeRepository.findByMatchIdAndUserId(matchId, sessionUser.getId()).isPresent();
        String countUser = chatRoomUserRepository.countByChatRoomId(matchPS.getChatRoom().getId()).toString();

        log.info("조회 결과 - isOwner: {}, likeCount: {}, isLike: {}, 참여자 수: {}",
                isOwner, likeCount, isLike, countUser);

        MatchResponse.DetailDTO detailDTO = new MatchResponse.DetailDTO(matchPS, isOwner, relativeTime, likeCount, isLike, countUser);
        return detailDTO;
    }


    // 매칭글 수정
    @Transactional
    public Object update(Integer matchId, MatchRequest.UpdateDTO reqDTO, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        Match matchPS = null;
        Game gamePS = null;
        Team teamPS = null;

        log.debug("매칭글 수정 요청: matchId={}, userId={}, reqDTO={}", matchId, sessionUser.getId(), reqDTO);


        // 매칭글 조회
        if (!(matchId == null)) {
            matchPS = matchRepository.findById(matchId)
                    .orElseThrow(() -> {
                        log.warn("매칭글 조회 실패: 존재하지 않음. matchId={}", matchId);
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
        }

        // 매칭글 삭제 여부 확인
        if (matchPS.getDeleteStatus() == DeleteStatus.DELETED) {
            log.warn("매칭글은 삭제된 상태입니다. matchId={}", matchId);
            throw new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
        }

        // 매칭글 권한 조회
        if (!(matchPS.getUser().getId().equals(sessionUser.getId()))) {
            log.warn("권한 없음. matchId={}, requesterId={}, ownerId={}",
                    matchId, sessionUser.getId(), matchPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 수정 정보 조회
        // 수정하려는 game이 없으면 or 선택한 게임이 이미 끝난 경기이면
        if (!(reqDTO.getGameId() == null)) {
            gamePS = gameRepository.findById(reqDTO.getGameId())
                    .orElseThrow(() -> {
                        log.warn("경기 조회 실패: gameId={}", reqDTO.getGameId());
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });

            if (gamePS.getGameTime().before(new Timestamp(System.currentTimeMillis()))) {
                log.warn("지난 경기로 수정 불가: gameId={}, gameTime={}", gamePS.getId(), gamePS.getGameTime());
                throw new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
            }
        }

        // 채팅방 조회
        Integer chatRoomId = matchPS.getChatRoom().getId();
        ChatRoom chatRoomPS = chatRoomRepository.findById(matchPS.getChatRoom().getId())
                .orElseThrow(() -> {
                    log.warn("채팅방 조회 실패: chatRoomId={}", chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        if (chatRoomPS.getDeleteStatus().equals(DeleteStatus.DELETED)) {
            log.warn("삭제된 채팅방입니다. chatRoomId={}", chatRoomPS.getId());
            throw new RuntimeException("해당 자원을 찾을 수 없습니다.");
        }

        // 조회한 팀이 없으면 throw
        if (!(reqDTO.getTeamId() == null)) {
            teamPS = teamRepository.findById(reqDTO.getTeamId())
                    .orElseThrow(() -> {
                        log.warn("팀 조회 실패: teamId={}", reqDTO.getTeamId());
                        return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                    });
        }
        String relativeTime = p.format(new Date(matchPS.getCreatedAt().getTime()));

        log.debug("채팅방 및 매칭글 업데이트 시작");
        // null 처리는 update 함수 내부에 되어 있음
        chatRoomPS.update(gamePS, teamPS, reqDTO);
        matchPS.update(chatRoomPS, reqDTO.getTitle(), reqDTO.getContent());

        // 부가 정보
        Boolean isOwner = true;
        Integer likeCount = matchLikeRepository.totalCount(matchId);
        Boolean isLike = matchLikeRepository.findByMatchIdAndUserId(matchId, sessionUser.getId()).isPresent();
        String countUser = chatRoomUserRepository.countByChatRoomId(matchPS.getChatRoom().getId()).toString();

        // 최종 dto
        MatchResponse.UpdateDTO respDTO = new MatchResponse.UpdateDTO(matchPS, isOwner, relativeTime, likeCount, isLike, countUser);

        // 반환
        log.info("매칭글 수정 성공: matchId={}, userId={}", matchId, sessionUser.getId());
        return respDTO;
    }

    // 매칭글 삭제
    @Transactional
    public Object delete(Integer matchId, User sessionUser) {
        log.info("매칭글 삭제 요청: matchId={}, userId={}", matchId, sessionUser.getId());


        // 매칭글 조회
        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.warn("매칭글 조회 실패: matchId={}", matchId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 매칭글에 대한 권한 조회
        if (!(matchPS.getUser().getId().equals(sessionUser.getId()))) {
            log.warn("매칭글 삭제 권한 없음: matchId={}, 요청자 userId={}, 작성자 userId={}",
                    matchId, sessionUser.getId(), matchPS.getUser().getId());
            throw new ExceptionApi403("해당 자원에 대한 권한이 없습니다.");
        }

        // 채팅방도 함께 삭제해야 함.
        Integer chatRoomId = matchPS.getChatRoom().getId();
        ChatRoom chatRoomPS = chatRoomRepository.findById(matchPS.getChatRoom().getId())
                .orElseThrow(() -> {
                    log.warn("채팅방 조회 실패: chatRoomId={}", chatRoomId);
                    return new ExceptionApi404("해당 자원을 찾을 수 없습니다.");
                });

        // 채팅방 유저 목록 조회
        List<ChatRoomUser> chatRoomUsersPS = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(matchPS.getChatRoom().getId());
        log.info("삭제 대상 채팅방 유저 수: {}", chatRoomUsersPS.size());

        // delete 함수 호출 -> DeleteStatus 상태 변경
        matchPS.delete();
        chatRoomPS.delete();
        for (ChatRoomUser chatRoomUser : chatRoomUsersPS) {
            chatRoomUser.delete();
        }
        log.info("매칭글 및 채팅방 삭제 완료: matchId={}, chatRoomId={}", matchId, chatRoomId);

        // delete 상태 전달
        MatchResponse.DeleteDTO respDTO = new MatchResponse.DeleteDTO(matchPS.getDeleteStatus());
        return respDTO;
    }
}
