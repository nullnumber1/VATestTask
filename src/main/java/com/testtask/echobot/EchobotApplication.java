package com.testtask.echobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:bot.properties")
public class EchobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchobotApplication.class, args);
    }

}
