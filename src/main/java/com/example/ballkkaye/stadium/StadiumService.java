package com.example.ballkkaye.stadium;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StadiumService {
    private final StadiumRepository stadiumRepository;


    public List<StadiumResponse.ListDTO> getStadiums() {
        List<Stadium> stadiums = stadiumRepository.findAll();
        List<StadiumResponse.ListDTO> respDTO = new ArrayList<>();
        for (Stadium s : stadiums) {
            respDTO.add(new StadiumResponse.ListDTO(s));
        }
        return respDTO;
    }
}
