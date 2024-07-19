package com.alejua.ratelimitednotificator.test.service;

import com.alejua.ratelimitednotificator.src.domain.NotificationType;
import com.alejua.ratelimitednotificator.src.service.NotificationServiceImpl;
import com.alejua.ratelimitednotificator.test.mocks.gateway.MockedNotificationGateway;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NotificatorServiceImplTest {

    @Test
    void sendShouldCallToGateway() {
        MockedNotificationGateway notificationGateway = new MockedNotificationGateway();
        NotificationServiceImpl service = new NotificationServiceImpl(notificationGateway);

        service.send(NotificationType.UPDATE, "userID", "msg 1");

        Assertions.assertEquals(1, notificationGateway.getSendCalledTimes());
        
        List<Map.Entry<String, String>> messages = notificationGateway.getMessages();
        Assertions.assertEquals(1, messages.size());
        Assertions.assertNotNull(messages.getFirst());
        Assertions.assertEquals("msg 1", messages.getFirst().getValue());
        Assertions.assertEquals("userID", messages.getFirst().getKey());
    }
}