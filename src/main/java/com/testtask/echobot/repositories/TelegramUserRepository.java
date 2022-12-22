package com.testtask.echobot.repositories;

import com.testtask.echobot.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
  //  TelegramUser findByTelegramId(Long telegramId);

}
