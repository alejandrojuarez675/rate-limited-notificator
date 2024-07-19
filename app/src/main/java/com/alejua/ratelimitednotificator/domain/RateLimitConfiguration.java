package com.alejua.ratelimitednotificator.domain;

import java.util.HashMap;
import java.util.Map;

public class RateLimitConfiguration {

    public static final RateLimitConfiguration WITHOUT_LIMIT = new RateLimitConfiguration(new HashMap<>());

    private final Map<NotificationType, RateLimitConfigurationRule> rulesByType;

    public RateLimitConfiguration(Map<NotificationType, RateLimitConfigurationRule> rulesByType) {
        this.rulesByType = rulesByType;
    }

    public Map<NotificationType, RateLimitConfigurationRule> getRulesByType() {
        return rulesByType;
    }
}
