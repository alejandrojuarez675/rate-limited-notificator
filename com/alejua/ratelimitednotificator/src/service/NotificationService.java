package com.alejua.ratelimitednotificator.src.service;

import com.alejua.ratelimitednotificator.src.domain.NotificationType;

public interface NotificationService {
    void send(NotificationType type, String userId, String message);
}
