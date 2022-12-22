package com.testtask.echobot.dao;

import lombok.Data;

@Data
public class EchoMessageDao {
    private String message;
    private Long userSender;
}
