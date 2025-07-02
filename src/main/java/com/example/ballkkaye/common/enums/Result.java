package com.example.ballkkaye.common.enums;

public enum Result {
    WIN("승"),
    LOSE("패"),
    TIE("무");

    private final String value;

    Result(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Result fromValue(String value) {
        for (Result result : Result.values()) {
            if (result.value.equals(value)) {
                return result;
            }
        }
        // 일치하는 값이 없을 경우 예외 처리 또는 null 반환 (여기서는 예외 처리)
        throw new IllegalArgumentException("승, 무, 패 외에는 들어갈 수 없습니다.");
    }
}
