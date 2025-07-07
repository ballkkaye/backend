package com.example.ballkkaye._core.util;

public class HitterLineupUtil {

    private static final int C = 20; // 임계값 (신뢰도 보정)

    /**
     * 베이지안 평균으로 안타 확률 계산
     */
    public static double calculateHitProbability(double seasonAvg, int matchupAb, double matchupAvg) {
        if (matchupAb == 0) return Math.round(seasonAvg * 1000) / 10.0;

        double raw = (C * seasonAvg + matchupAb * matchupAvg) / (C + matchupAb);
        return Math.round(raw * 1000) / 10.0;  // 예: 0.29126 → 29.1
    }
}
