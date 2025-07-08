package com.example.ballkkaye.match;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));
        if (gamePS.getGameTime().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("이미 지난 경기는 매칭할 수 없습니다");
        }
        System.out.println(reqDTO.getPreferredAge());
        System.out.println(reqDTO.getPreferredGender());
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


        var chatReeomUser = chatRoomUserRepository.findByUserIdAndChatRoomId(userPS.getId(), chatRoom.getId());
        if (chatReeomUser.isPresent()) {
            throw new RuntimeException("이미 해당 채팅방에 참여중입니다.");
        }

        chatRoomUserRepository.save(
                new ChatRoomUser()
                        .builder()
                        .chatRoom(chatRoom)
                        .user(userPS)
                        .isOwner(true)
                        .deleteStatus(DeleteStatus.NOT_DELETED)
                        .build());


        Match match = new Match().builder()
                .user(userPS)
                .chatRoom(chatRoom)
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .build();
        matchRepository.save(match);

        ChatRoomResponse.DTO chatRoomDTO = new ChatRoomResponse.DTO(chatRoom);
        MatchResponse.DTO matchDTO = new MatchResponse.DTO(match);
        MatchResponse.SaveDTO respDTO = new MatchResponse.SaveDTO(chatRoomDTO, matchDTO);
        System.out.println("===========================");
        System.out.println("===========================");
        System.out.println(chatRoomDTO.getId());
        System.out.println(matchDTO.getId());
        System.out.println("===========================");
        System.out.println("===========================");

        return respDTO;
    }

    // 동행글 목록 조회
    public Object getMatches(User sessionUser, Integer page, Gender gender, Age age, Integer teamId) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        String selectedGender = gender == null ? null : gender.getLabel();
        String selectedTimeName = null;
        String selectedAge = age == null ? null : age.getName();
        if (teamId != null) {
            Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
            selectedTimeName = team.getTeamName();
        }

        String genderStr = (gender != null) ? gender.name() : null;
        String ageStr = (age != null) ? age.name() : null;
        List<Match> matches = matchRepository.findAll(page, genderStr, ageStr, teamId);

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

    public Object getDetail(Integer matchId, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        String relativeTime = p.format(new Date(matchPS.getCreatedAt().getTime()));
        Boolean isOwner = false;
        if (matchPS.getUser().getId().equals(sessionUser.getId())) {
            isOwner = true;
        }
        Integer likeCount = matchLikeRepository.totalCount(matchId);
        Boolean isLike = matchLikeRepository.findByMatchIdAndUserId(matchId, sessionUser.getId()).isPresent();
        String countUser = chatRoomUserRepository.countByChatRoomId(matchPS.getChatRoom().getId()).toString();


        MatchResponse.DetailDTO detailDTO = new MatchResponse.DetailDTO(matchPS, isOwner, relativeTime, likeCount, isLike, countUser);
        return detailDTO;
    }


    // 매칭글 수정
    @Transactional
    public Object update(Integer matchId, MatchRequest.@Valid UpdateDTO reqDTO, User sessionUser) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        Match matchPS = null;
        Game gamePS = null;
        Team teamPS = null;

        // 매칭글 조회
        if (!(matchId == null)) {
            matchPS = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        }

        // 매칭글 삭제 여부 확인
        if (matchPS.getDeleteStatus() == DeleteStatus.DELETED) {
            throw new RuntimeException("해당 자원을 찾을 수 없습니다.");
        }

        // 매칭글 권한 조회
        if (!(matchPS.getUser().getId().equals(sessionUser.getId()))) {
            throw new RuntimeException("해당 자원에 대한 권한이 없습니다.");
        }

        // 수정 정보 조회
        // 수정하려는 game이 없으면 or 선택한 게임이 이미 끝난 경기이면
        if (!(reqDTO.getGameId() == null)) {
            gamePS = gameRepository.findById(reqDTO.getGameId())
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            if (gamePS.getGameTime().before(new Timestamp(System.currentTimeMillis()))) {
                throw new RuntimeException("해당 자원을 찾을 수 없습니다.");
            }
        }

        // 채팅방 조회
        ChatRoom chatRoomPS = chatRoomRepository.findById(matchPS.getChatRoom().getId())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        if (chatRoomPS.getDeleteStatus().equals(DeleteStatus.DELETED)) {
            throw new RuntimeException("해당 자원을 찾을 수 없습니다.");
        }

        // 조회한 팀이 없으면 throw
        if (!(reqDTO.getTeamId() == null)) {
            teamPS = teamRepository.findById(reqDTO.getTeamId())
                    .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));
        }
        String relativeTime = p.format(new Date(matchPS.getCreatedAt().getTime()));

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
        return respDTO;
    }

    // 매칭글 삭제
    @Transactional
    public Object delete(Integer matchId, User sessionUser) {
        // 매칭글 조회
        Match matchPS = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 매칭글에 대한 권한 조회
        if (!(matchPS.getUser().getId().equals(sessionUser.getId()))) {
            throw new RuntimeException("해당 자원에 대한 권한이 없습니다.");
        }

        // 채팅방도 함께 삭제해야 함.
        ChatRoom chatRoomPS = chatRoomRepository.findById(matchPS.getChatRoom().getId())
                .orElseThrow(() -> new RuntimeException("해당 자원을 찾을 수 없습니다."));

        // 채팅방 유저 목록 조회
        List<ChatRoomUser> chatRoomUsersPS = chatRoomUserRepository.findByChatRoomIdAndDeleteStatus(matchPS.getChatRoom().getId());

        // delete 함수 호출 -> DeleteStatus 상태 변경
        matchPS.delete();
        chatRoomPS.delete();
        for (ChatRoomUser chatRoomUser : chatRoomUsersPS) {
            chatRoomUser.delete();
        }

        // delete 상태 전달
        MatchResponse.DeleteDTO respDTO = new MatchResponse.DeleteDTO(matchPS.getDeleteStatus());
        return respDTO;
    }
}
