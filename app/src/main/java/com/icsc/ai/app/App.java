package com.icsc.ai.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        log.info("Starting application...");
        SpringApplication.run(App.class, args);
    }

    public Object getGreeting() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGreeting'");
    }
}
