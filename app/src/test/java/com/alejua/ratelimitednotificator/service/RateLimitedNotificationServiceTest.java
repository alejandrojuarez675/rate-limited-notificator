package com.alejua.ratelimitednotificator.service;

import static com.alejua.ratelimitednotificator.domain.NotificationType.MARKETING;
import static com.alejua.ratelimitednotificator.domain.NotificationType.NEWS;
import static com.alejua.ratelimitednotificator.domain.NotificationType.UPDATE;

import com.alejua.ratelimitednotificator.domain.RateLimitConfiguration;
import com.alejua.ratelimitednotificator.domain.RateLimitConfigurationRule;
import com.alejua.ratelimitednotificator.mocks.repository.MockedNotificationRepository;
import com.alejua.ratelimitednotificator.mocks.service.MockedNotificationServiceImpl;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RateLimitedNotificationServiceTest {

    public static final String USER_ID_1 = "user1";
    public static final String USER_ID_2 = "user2";

    @Test
    public void sendMsgToNotLimitedTypeShouldSendAllMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, RateLimitConfiguration.WITHOUT_LIMIT);

        for (int i = 0; i < 100; i++) {
            service.send(NEWS, USER_ID_1, "news " + i);
        }

        Assertions.assertEquals(100, notificationService.getCalledSendTimes());
        Assertions.assertEquals(100, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(0, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedForAnotherTypeShouldAllowSendAllMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for marketing
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                MARKETING, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        // but type is news, so for news is unlimited
        for (int i = 0; i < 100; i++) {
            service.send(NEWS, USER_ID_1, "news " + i);
        }

        Assertions.assertEquals(100, notificationService.getCalledSendTimes());
        Assertions.assertEquals(100, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(0, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldAllowSendMinusMsgThatAllowed() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for NEWS
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NEWS, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");

        Assertions.assertEquals(2, notificationService.getCalledSendTimes());
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(2, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldAllowSendEqualsQuantityMsgThatAllowed() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 3 msg each 1 minute for NEWS
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NEWS, new RateLimitConfigurationRule(3L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");
        service.send(NEWS, USER_ID_1,"news 3");

        Assertions.assertEquals(3, notificationService.getCalledSendTimes());
        Assertions.assertEquals(3, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(3, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldLimitMsg() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 2 msg each 1 minute for marketing
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NEWS, new RateLimitConfigurationRule(2L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");
        service.send(NEWS, USER_ID_1,"news 3");

        Assertions.assertEquals(2, notificationService.getCalledSendTimes());
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(2, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldLimitMsgOnlyForLimitedType() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        // 2 msg each 1 minute for marketing
        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                NEWS, new RateLimitConfigurationRule(2L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        for (int i = 0; i < 100; i++) {
            service.send(UPDATE, USER_ID_1, "update " + i);
        }

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");
        service.send(NEWS, USER_ID_1,"news 3");

        // 100 of update and 2 of news
        Assertions.assertEquals(102, notificationService.getCalledSendTimes());
        Assertions.assertEquals(100, notificationService.getCalledByTypeAndUser(USER_ID_1, UPDATE));
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(2, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldAllowSendToMoreThanOneLimitedType() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                UPDATE, new RateLimitConfigurationRule(1L, 60L),
                NEWS, new RateLimitConfigurationRule(2L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(UPDATE, USER_ID_1, "update 1");
        service.send(UPDATE, USER_ID_1, "update 2");

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");
        service.send(NEWS, USER_ID_1,"news 3");

        service.send(MARKETING, USER_ID_1, "marketing 1");
        service.send(MARKETING, USER_ID_1, "marketing 2");

        Assertions.assertEquals(5, notificationService.getCalledSendTimes());
        Assertions.assertEquals(1, notificationService.getCalledByTypeAndUser(USER_ID_1, UPDATE));
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, MARKETING));
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));
        Assertions.assertEquals(3, notificationRepository.getCalledSaveNotificationTimes());
    }

    @Test
    public void sendMsgToLimitedTypeShouldBeOkWithDifferentTypesAndUsers() {
        MockedNotificationServiceImpl notificationService = new MockedNotificationServiceImpl();
        MockedNotificationRepository notificationRepository = new MockedNotificationRepository();

        RateLimitConfiguration conf = new RateLimitConfiguration(Map.of(
                UPDATE, new RateLimitConfigurationRule(1L, 60L),
                NEWS, new RateLimitConfigurationRule(2L, 60L)
        ));

        RateLimitedNotificationServiceImpl service = new RateLimitedNotificationServiceImpl(
                notificationService, notificationRepository, conf);

        service.send(UPDATE, USER_ID_1, "update 1");
        service.send(UPDATE, USER_ID_1, "update 2"); // limited

        service.send(NEWS, USER_ID_1,"news 1");
        service.send(NEWS, USER_ID_1,"news 2");
        service.send(NEWS, USER_ID_1,"news 3"); // limited

        service.send(MARKETING, USER_ID_1, "marketing 1");
        service.send(MARKETING, USER_ID_1, "marketing 2");

        service.send(UPDATE, USER_ID_2, "update 1");
        service.send(UPDATE, USER_ID_2, "update 2"); // limited
        service.send(MARKETING, USER_ID_2, "marketing 1");
        service.send(NEWS, USER_ID_2,"news 1");

        Assertions.assertEquals(8, notificationService.getCalledSendTimes());

        Assertions.assertEquals(1, notificationService.getCalledByTypeAndUser(USER_ID_1, UPDATE));
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, MARKETING));
        Assertions.assertEquals(2, notificationService.getCalledByTypeAndUser(USER_ID_1, NEWS));

        Assertions.assertEquals(1, notificationService.getCalledByTypeAndUser(USER_ID_2, UPDATE));
        Assertions.assertEquals(1, notificationService.getCalledByTypeAndUser(USER_ID_2, MARKETING));
        Assertions.assertEquals(1, notificationService.getCalledByTypeAndUser(USER_ID_2, NEWS));

        Assertions.assertEquals(5, notificationRepository.getCalledSaveNotificationTimes());
    }
}