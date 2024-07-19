package com.alejua.ratelimitednotificator.mocks.service;

import com.alejua.ratelimitednotificator.domain.NotificationType;
import com.alejua.ratelimitednotificator.service.NotificationService;
import java.util.HashMap;
import java.util.Map;

public class MockedNotificationServiceImpl implements NotificationService {

    private Long calledSendTimes = 0L;

    private final Map<String, Long> calledByTypeUser = new HashMap<>();

    @Override
    public void send(NotificationType type, String userId, String message) {
        addCalledToMemory(userId, type);
    }

    public Long getCalledSendTimes() {
        return calledSendTimes;
    }

    public Long getCalledByTypeAndUser(String userId, NotificationType type) {
        return calledByTypeUser.get(createKey(userId, type));
    }

    private void addCalledToMemory(String userId, NotificationType type) {
        calledSendTimes++;

        String key = createKey(userId, type);
        Long calledNotificationByTypeUser = calledByTypeUser.getOrDefault(key, 0L);
        calledNotificationByTypeUser++;
        calledByTypeUser.put(key, calledNotificationByTypeUser);
    }

    private String createKey(String userId, NotificationType type) {
        return userId + "-" + type;
    }
}
