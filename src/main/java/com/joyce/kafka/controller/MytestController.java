package com.joyce.kafka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MytestController {

    @RequestMapping("/health/shallow")
    public Map<String, Object> getHealth() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

}
