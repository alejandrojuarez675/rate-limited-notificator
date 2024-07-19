package com.alejua.ratelimitednotificator.test.mocks.gateway;

import com.alejua.ratelimitednotificator.src.gateway.NotificationGateway;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockedNotificationGateway implements NotificationGateway {

    private Long sendCalledTimes = 0L;
    private final List<Map.Entry<String, String>> messages = new ArrayList<>();

    @Override
    public void send(String userId, String message) {
        System.out.println("Sending the message: " + message + " to user " + userId);
        messages.add(Map.entry(userId, message));
        sendCalledTimes++;
    }

    public Long getSendCalledTimes() {
        return sendCalledTimes;
    }

    public List<Map.Entry<String, String>> getMessages() {
        return messages;
    }
}
