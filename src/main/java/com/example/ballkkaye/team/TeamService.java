package com.example.ballkkaye.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public List<TeamResponse.ListDTO> getTeams() {
        List<Team> teams = teamRepository.findAll();
        List<TeamResponse.ListDTO> respDTO = new ArrayList<>();
        for (Team team : teams) {
            respDTO.add(new TeamResponse.ListDTO(team.getId(), team.getTeamName()));
        }
        return respDTO;
    }
}
