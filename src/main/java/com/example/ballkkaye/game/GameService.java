package com.example.ballkkaye.game;

import com.example.ballkkaye._core.error.ex.ExceptionApi400;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;

    public Object getGames(String date) {
        log.info("[경기 리스트 조회 요청] date={}", date);

        // date 검사
        if (date == null || date.isBlank()) {
            throw new ExceptionApi400("잘못된 요청입니다.");
        }

        // 게임 조회
        List<Game> games = gameRepository.findByDate(date);
        log.info("조회된 경기 수: {}", games.size());

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

        log.info("응답 DTO 구성 완료 - group count: {}", groupedList.size());

        // respDTO 에 담음
        GameResponse.ListDTO respDTO = new GameResponse.ListDTO(date, groupedList);
        return respDTO;
    }

    public Object getCalendarGames(String date) {
        List<String> dateList;

        if (date != null && !date.isBlank() && date.length() == 7) {
            log.info("월 단위 조회 요청: {}", date);
            dateList = gameRepository.findDistinctDatesByMonth(date);
        } else if (date != null && !date.isBlank() && date.length() == 4) {
            log.info("연 단위 조회 요청: {}", date);
            dateList = gameRepository.findDistinctDatesByMonth(date);
        } else {
            log.info("전체 조회 요청 (date 파라미터 없음 또는 형식 불일치)");
            dateList = gameRepository.findDistinctDatesByMonth();
        }

        log.info("조회된 날짜 수: {}", dateList.size());

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

        log.info("연도 수: {}, 첫 연도: {}", groupedMap.size(), groupedMap.keySet().stream().findFirst().orElse("없음"));


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

        log.info("최종 반환되는 연도 개수: {}", result.size());
        return result;
    }
}
