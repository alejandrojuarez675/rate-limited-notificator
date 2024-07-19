package com.alejua.ratelimitednotificator.service;

import com.alejua.ratelimitednotificator.domain.Notification;
import com.alejua.ratelimitednotificator.domain.NotificationType;
import com.alejua.ratelimitednotificator.domain.RateLimitConfiguration;
import com.alejua.ratelimitednotificator.domain.RateLimitConfigurationRule;
import com.alejua.ratelimitednotificator.repository.NotificationRepository;
import java.util.List;

public class RateLimitedNotificationServiceImpl implements NotificationService {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final RateLimitConfiguration rateLimitConfiguration;

    public RateLimitedNotificationServiceImpl(
            NotificationService notificationService,
            NotificationRepository notificationRepository,
            RateLimitConfiguration rateLimitConfiguration) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.rateLimitConfiguration = rateLimitConfiguration;
    }

    @Override
    public void send(NotificationType type, String userId, String message) {
        RateLimitConfigurationRule rule = rateLimitConfiguration.getRulesByType().get(type);
        if (rule == null) { // not limited case
            notificationService.send(type, userId, message);
            return;
        }

        List<Notification> notifications = notificationRepository.getLastInSeconds(type, userId, rule.getTimeInSeconds());
        if (notifications.size() >= rule.getAllowedQuantity()) {
            System.out.println("Max allowed limit was exceeded");
            // queue logic to retry after? it depends on business logic
            // exception to notify to the front the problem ?
            // notify in same channel the problem ?
            // do nothing ? this is what I did here
            return;
        }

        notificationService.send(type, userId, message);
        notificationRepository.saveNotification(new Notification(userId, type));
    }
}
