package com.alejua.ratelimitednotificator.src.domain;

public class RateLimitConfigurationRule {

    private final Long allowedQuantity;
    private final Long timeInSeconds;

    public RateLimitConfigurationRule(Long allowedQuantity, Long timeUnitInSeconds) {
        this.allowedQuantity = allowedQuantity;
        this.timeInSeconds = timeUnitInSeconds;
    }

    public Long getAllowedQuantity() {
        return allowedQuantity;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }
}
