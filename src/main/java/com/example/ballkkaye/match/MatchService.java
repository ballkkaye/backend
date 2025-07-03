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

    // 동행글 작성
    @Transactional
    public Object save(User sessionUser, MatchRequest.@Valid SaveDTO reqDTO) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));
        if (gamePS.getGameTime().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("이미 지난 경기는 매칭할 수 없습니다");
        }

        ChatRoom chatRoom = new ChatRoom().builder()
                .game(gamePS)
                .team(teamPS)
                .maxParticipants(reqDTO.getMaxParticipants())
                .preferredGender(reqDTO.getPreferredGender())
                .preferredAge(reqDTO.getPreferredAge())
                .preferredRegion(reqDTO.getPreferredRegion())
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

        return respDTO;
    }

    // 동행글 목록 조회
    public Object getMatches(User sessionUser, Integer page, Gender gender, Age age, Integer teamId) {
        PrettyTime p = new PrettyTime(Locale.KOREAN);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
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

        MatchResponse.ListDTO respDTO = new MatchResponse.ListDTO(gender.getLabel(), age.getName(), team.getTeamName(), items);
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
        MatchResponse.DetailDTO detailDTO = new MatchResponse.DetailDTO(matchPS, isOwner, relativeTime);
        return detailDTO;
    }
}
