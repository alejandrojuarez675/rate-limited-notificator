package com.alejua.ratelimitednotificator.src.service;

import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import com.alejua.ratelimitednotificator.src.gateway.NotificationGateway;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationGateway notificationGateway;

    public NotificationServiceImpl(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    @Override
    public void send(NotificationType type, String userId, String message) {
        // with the type, we probably do some logic that customize the message
        notificationGateway.send(userId, message);
    }
}
