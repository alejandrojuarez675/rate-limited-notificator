package com.alejua.ratelimitednotificator.src.repository;

import com.alejua.ratelimitednotificator.src.domain.Notification;
import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import java.util.List;

public interface NotificationRepository {
    void saveNotification(Notification notification);

    List<Notification> getLastInSeconds(NotificationType type, String userId, Long quantityOfSeconds);
}
