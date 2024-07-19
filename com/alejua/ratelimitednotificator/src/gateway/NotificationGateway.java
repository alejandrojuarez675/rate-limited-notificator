package com.alejua.ratelimitednotificator.src.gateway;

public interface NotificationGateway {
    void send(String userId, String message);
}
