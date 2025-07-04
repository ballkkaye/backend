package com.example.ballkkaye.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;

    public Object getGames(String date) {
        // date 검사
        System.out.println("================");
        System.out.println(date);
        if (date == null || date.isBlank()) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        // 게임 조회
        List<Game> games = gameRepository.findByDate(date);
        for (Game game : games) {
            System.out.println("--------------");
            System.out.println(game.getHomeTeam());
        }

        // 게임 item에 담기
        List<GameResponse.GroupedByDateDTO.ItemDTO> itemList = new ArrayList<>();
        for (Game game : games) {
            itemList.add(new GameResponse.GroupedByDateDTO.ItemDTO(game));
        }

        // 경기 날짜로 묶음
        Map<String, List<GameResponse.GroupedByDateDTO.ItemDTO>> groupMap = new LinkedHashMap<>();
        for (GameResponse.GroupedByDateDTO.ItemDTO item : itemList) {
            String gameDate = item.getGameDate();
            if (!groupMap.containsKey(gameDate)) {
                groupMap.put(gameDate, new ArrayList<>());
            }
            groupMap.get(gameDate).add(item);
        }


        // dto에 담기
        List<GameResponse.GroupedByDateDTO> groupedList = new ArrayList<>();
        for (String gameDate : groupMap.keySet()) {
            List<GameResponse.GroupedByDateDTO.ItemDTO> items = groupMap.get(gameDate);
            groupedList.add(new GameResponse.GroupedByDateDTO(gameDate, items));
        }

        // respDTO 에 담음
        GameResponse.ListDTO respDTO = new GameResponse.ListDTO(date, groupedList);
        return respDTO;
    }
}