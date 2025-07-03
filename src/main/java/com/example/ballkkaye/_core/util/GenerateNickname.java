package com.example.ballkkaye._core.util;

import com.example.ballkkaye.common.enums.NicknamePrefix;

import java.util.Random;

public class GenerateNickname {
    public static String create() {
        NicknamePrefix[] values = NicknamePrefix.values();
        Random random = new Random();

        NicknamePrefix randomPrefix = values[random.nextInt(values.length)];
        int randomNumber = random.nextInt(1000000); // 0 ~ 999999

        // 필요 시 6자리 고정 숫자로 만들기
        return randomPrefix.getValue() + String.format("%06d", randomNumber);
    }
}
