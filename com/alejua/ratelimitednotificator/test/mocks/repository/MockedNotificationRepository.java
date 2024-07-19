package com.alejua.ratelimitednotificator.test.mocks.repository;

import com.alejua.ratelimitednotificator.src.domain.Notification;
import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import com.alejua.ratelimitednotificator.src.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockedNotificationRepository implements NotificationRepository {

    private final Map<LocalDateTime, Notification> data = new HashMap<>();
    private Long calledSaveNotificationTimes = 0L;

    @Override
    public void saveNotification(Notification notification) {
        data.put(LocalDateTime.now(), notification);
        calledSaveNotificationTimes++;
    }

    @Override
    public List<Notification> getLastInSeconds(NotificationType type, String userId, Long quantityOfSeconds) {
        return data.entrySet().stream()
                .filter(entry -> filterNotificationPerDataAndTime(entry, type, userId, quantityOfSeconds))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private boolean filterNotificationPerDataAndTime(
            Map.Entry<LocalDateTime, Notification> entry,
            NotificationType type,
            String userId,
            Long quantityOfSeconds) {
        Notification notification = entry.getValue();
        boolean isSameUserId = userId.equals(notification.getUserId());
        boolean isSameType = type.equals(notification.getType());
        boolean isInTimePeriod = LocalDateTime.now().minusSeconds(quantityOfSeconds).isBefore(entry.getKey());
        return isSameUserId && isSameType && isInTimePeriod;
    }

    public Long getCalledSaveNotificationTimes() {
        return calledSaveNotificationTimes;
    }
}
