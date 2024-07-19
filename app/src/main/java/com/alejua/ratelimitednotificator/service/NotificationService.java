package com.alejua.ratelimitednotificator.service;

import com.alejua.ratelimitednotificator.domain.NotificationType;

public interface NotificationService {
    void send(NotificationType type, String userId, String message);
}
