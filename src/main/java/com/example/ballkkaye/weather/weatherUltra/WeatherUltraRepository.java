package com.example.ballkkaye.weather.weatherUltra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class WeatherUltraRepository {

    private final EntityManager em;


    /**
     * 특정 구장의 오늘 날짜 예보 데이터를 조회 (예보 시각 기준 정렬)
     */
    public List<WeatherUltra> findTodayForecastByStadiumId(Integer stadiumId) {
        LocalDate today = LocalDate.now();
        Timestamp start = Timestamp.valueOf(today.atStartOfDay());
        Timestamp end = Timestamp.valueOf(today.plusDays(1).atStartOfDay());

        return em.createQuery("""
                            SELECT w
                            FROM WeatherUltra w
                            WHERE w.stadium.id = :stadiumId
                              AND w.forecastAt >= :start
                              AND w.forecastAt < :end
                            ORDER BY w.forecastAt ASC
                        """, WeatherUltra.class)
                .setParameter("stadiumId", stadiumId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }


    /**
     * 전날 같은 시간대(시 기준)의 예보 데이터를 조회하는 메서드
     */
    public Optional<WeatherUltra> findYesterdayForecastSameHour(Integer stadiumId, int forecastHour, LocalDate yesterday) {
        try {

            LocalDateTime start = yesterday.atTime(forecastHour, 0);
            LocalDateTime end = start.plusHours(1);
            return em.createQuery("""
                            SELECT w FROM WeatherUltra w
                            WHERE w.stadium.id = :stadiumId
                              AND w.forecastAt >= :start
                              AND w.forecastAt < :end
                            ORDER BY w.forecastAt DESC
                            """, WeatherUltra.class)
                    .setParameter("stadiumId", stadiumId)
                    .setParameter("start", Timestamp.valueOf(start))
                    .setParameter("end", Timestamp.valueOf(end))
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // 일괄 저장
    public void saveAll(List<WeatherUltra> weatherUltraList) {
        for (WeatherUltra w : weatherUltraList) {
            em.persist(w);
        }
    }

    
}
