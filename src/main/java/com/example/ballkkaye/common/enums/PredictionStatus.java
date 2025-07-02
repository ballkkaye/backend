package com.example.ballkkaye.common.enums;

public enum PredictionStatus {
    CORRECT("정답"),
    INCORRECT("오답"),
    CANCELLED("경기 취소"); // 경기 취소 상태 추가

    private final String value;

    PredictionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}