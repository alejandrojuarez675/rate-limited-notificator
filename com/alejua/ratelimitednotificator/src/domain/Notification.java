package com.alejua.ratelimitednotificator.src.domain;

public class Notification {

    private final String userId;
    private final NotificationType type;

    public Notification(String userId, NotificationType type) {
        this.userId = userId;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public NotificationType getType() {
        return type;
    }
}
