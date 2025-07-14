package com.example.ballkkaye._core.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionManager {

    private final Map<Integer, Set<Integer>> roomSubscribers = new ConcurrentHashMap<>();

    public synchronized boolean addSubscriber(Integer chatRoomId, Integer userId) {
        roomSubscribers.putIfAbsent(chatRoomId, ConcurrentHashMap.newKeySet());
        Set<Integer> users = roomSubscribers.get(chatRoomId);
        if (users.contains(userId)) {
            return false; // 이미 구독함
        }
        users.add(userId);
        return true; // 새로 구독함
    }

    public synchronized void removeSubscriber(Integer chatRoomId, Integer userId) {
        Set<Integer> users = roomSubscribers.get(chatRoomId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                roomSubscribers.remove(chatRoomId);
            }
        }
    }
}

