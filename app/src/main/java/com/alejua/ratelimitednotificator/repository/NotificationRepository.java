package com.alejua.ratelimitednotificator.repository;

import com.alejua.ratelimitednotificator.domain.Notification;
import com.alejua.ratelimitednotificator.domain.NotificationType;
import java.util.List;

public interface NotificationRepository {
    void saveNotification(Notification notification);

    List<Notification> getLastInSeconds(NotificationType type, String userId, Long quantityOfSeconds);
}
