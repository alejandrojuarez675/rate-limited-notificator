package com.alejua.ratelimitednotificator.test.mocks.service;

import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import com.alejua.ratelimitednotificator.src.service.NotificationService;

public class MockedNotificationServiceImpl implements NotificationService {

    private Long calledSendTimes = 0L;

    @Override
    public void send(NotificationType type, String userId, String message) {
        calledSendTimes++;
    }

    public Long getCalledSendTimes() {
        return calledSendTimes;
    }
}
