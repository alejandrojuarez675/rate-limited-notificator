package com.alejua.ratelimitednotificator.gateway;

public interface NotificationGateway {
    void send(String userId, String message);
}
