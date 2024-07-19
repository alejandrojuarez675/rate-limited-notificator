package com.alejua.ratelimitednotificator.test.service;

import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import com.alejua.ratelimitednotificator.src.domain.RateLimitConfiguration;
import com.alejua.ratelimitednotificator.src.domain.RateLimitConfigurationRule;
import com.alejua.ratelimitednotificator.src.service.RateLimitedNotificationServiceImpl;
import com.alejua.ratelimitednotificator.test.mocks.repository.MockedNotificationRepository;
import com.alejua.ratelimitednotificator.test.mocks.service.MockedNotificationServiceImpl;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RateLimitedNotificationServiceTest {

    @Test
    public void sendMsgToNotLimitedTypeShouldSendAllMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, RateLimitConfiguration.WITHOUT_LIMIT);

        for (int i = 0; i < 100; i++) {
            service.send(NotificationType.NEWS, "user", "news " + i);
        }

        Assertions.assertEquals(100, notificationService.getCalledSendTimes());
        Assertions.assertEquals(0, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedForAnotherTypeShouldAllowSendAllMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for marketing
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NotificationType.MARKETING, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        // but type is news, so for news is unlimited
        for (int i = 0; i < 100; i++) {
            service.send(NotificationType.NEWS, "user", "news " + i);
        }

        Assertions.assertEquals(100, notificationService.getCalledSendTimes());
        Assertions.assertEquals(0, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldAllowSendMinusMsgThatAllowed() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for NEWS
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NotificationType.NEWS, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NotificationType.NEWS,"user","news 1");
        service.send(NotificationType.NEWS,"user","news 2");

        Assertions.assertEquals(2, notificationService.getCalledSendTimes());
        Assertions.assertEquals(2, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldAllowSendEqualsQuantityMsgThatAllowed() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for NEWS
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NotificationType.NEWS, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NotificationType.NEWS,"user","news 1");
        service.send(NotificationType.NEWS,"user","news 2");
        service.send(NotificationType.NEWS,"user","news 3");

        Assertions.assertEquals(3, notificationService.getCalledSendTimes());
        Assertions.assertEquals(3, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldLimitMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 2 msg each 1 minute for marketing
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NotificationType.NEWS, new RateLimitConfigurationRule(2L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NotificationType.NEWS,"user","news 1");
        service.send(NotificationType.NEWS,"user","news 2");
        service.send(NotificationType.NEWS,"user","news 3");

        Assertions.assertEquals(2, notificationService.getCalledSendTimes());
        Assertions.assertEquals(2, notificationRepository.getCalledSaveNotificationTimes());
    }

    // TODO test with two or more types and combinations
    // TODO test with two or more users and combinations

}