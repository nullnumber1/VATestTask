package com.testtask.echobot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "telegram_users")
@Data
public class TelegramUser {

    @Id
    @Column(name = "telegram_id")
    private Long id;
    @Column(name = "request_count")
    private Long requestCount;
}
