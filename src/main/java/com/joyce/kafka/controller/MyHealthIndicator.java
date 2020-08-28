package com.joyce.kafka.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * localhost:8080/actuator/health
 */
@Component("myHealth")
public class MyHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up().withDetail("msg", "I'm a health checking test.").build();
    }
}
