package com.example.ballkkaye._core.util;

public class TodayGameUtil {


    /**
     * Double 값을 소수점 첫째 자리까지 반올림하여 반환한다.
     * - null 입력 시 0.0 반환
     */
    public static double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10) / 10.0;
    }


    /**
     * 전체 구장 이름을 간략화된 이름으로 변환한다.
     * - ex) "잠실야구장" → "잠실", "대전 한화생명이글스파크" → "대전"
     * - 미등록된 이름은 그대로 반환
     */
    public static String simplifyStadiumName(String fullName) {
        return switch (fullName.trim()) {
            case "잠실야구장" -> "잠실";
            case "고척스카이돔" -> "고척";
            case "수원 KT위즈파크" -> "수원";
            case "인천 SSG 랜더스필드" -> "문학";
            case "광주-기아 챔피언스필드" -> "광주";
            case "대구 삼성라이온즈파크" -> "대구";
            case "부산 사직야구장" -> "부산";
            case "대전 한화생명이글스파크" -> "대전";
            case "창원 NC파크" -> "창원";
            case "청주 야구장" -> "청주";
            case "울산 문수야구장" -> "울산";
            case "포항 야구장" -> "포항";
            case "군산 월명야구장" -> "군산";
            default -> fullName; // fallback
        };
    }


    /**
     * 전체 팀명을 팀명 앞 단어로 간략화하여 반환한다.
     * - ex) "두산 베어스" → "두산", "SSG 랜더스" → "SSG"
     */
    public static String simplifyTeamName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "";
        return fullName.trim().split(" ")[0];
    }
}
