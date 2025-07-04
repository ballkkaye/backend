package com.example.ballkkaye.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Object getCalendarGames(String date) {
        List<String> dateList;

        if (date != null && !date.isBlank() && date.length() == 7) {
            dateList = gameRepository.findDistinctDatesByMonth(date);
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        Map<String, Map<String, Set<String>>> groupedMap = new TreeMap<>();

        for (String fullDate : dateList) {
            String[] parts = fullDate.split("-");
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];

            groupedMap
                    .computeIfAbsent(year, y -> new TreeMap<>())
                    .computeIfAbsent(month, m -> new TreeSet<>())
                    .add(day);
        }

        List<GameResponse.CalendarDTO> result = new ArrayList<>();

        for (String year : groupedMap.keySet()) {
            GameResponse.CalendarDTO calendarDTO = new GameResponse.CalendarDTO();
            calendarDTO.setYear(year);

            List<GameResponse.MonthDTO> monthDTOList = new ArrayList<>();
            for (String month : groupedMap.get(year).keySet()) {
                GameResponse.MonthDTO monthDTO = new GameResponse.MonthDTO();
                monthDTO.setMonth(month);

                List<GameResponse.DayDTO> dayList = new ArrayList<>();
                for (String day : groupedMap.get(year).get(month)) {
                    GameResponse.DayDTO dayDTO = new GameResponse.DayDTO();
                    dayDTO.setDay(day);
                    dayDTO.setIsHaveGame(true);
                    dayList.add(dayDTO);
                }

                monthDTO.setDay(dayList);
                monthDTOList.add(monthDTO);
            }

            calendarDTO.setMonthDTO(monthDTOList);
            result.add(calendarDTO);
        }

        return result;
    }
}