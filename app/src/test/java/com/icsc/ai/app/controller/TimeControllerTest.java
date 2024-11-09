package com.icsc.ai.app.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
class TimeControllerTest {

    @Test
    void testGetCurrentTime() {
        TimeController timeController = new TimeController();
        String time = timeController.getCurrentTime();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedTime = LocalDateTime.parse(time, formatter);
        long secondsDifference = java.time.Duration.between(parsedTime, now).getSeconds();
        boolean isWithinOneSecond = Math.abs(secondsDifference) < 1;
        assertTrue(isWithinOneSecond);
    }
}
