package com.example.ballkkaye.match;

import com.example.ballkkaye.common.enums.DeleteStatus;
import com.example.ballkkaye.game.Game;
import com.example.ballkkaye.game.GameRepository;
import com.example.ballkkaye.match.chat.room.ChatRoom;
import com.example.ballkkaye.match.chat.room.ChatRoomRepository;
import com.example.ballkkaye.match.chat.room.ChatRoomResponse;
import com.example.ballkkaye.team.Team;
import com.example.ballkkaye.team.TeamRepository;
import com.example.ballkkaye.user.User;
import com.example.ballkkaye.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Object save(User sessionUser, MatchRequest.@Valid SaveDTO reqDTO) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Game gamePS = gameRepository.findById(reqDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        Team teamPS = teamRepository.findById(reqDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

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
}
