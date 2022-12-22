package com.testtask.echobot.services;

import com.testtask.echobot.models.TelegramUser;
import com.testtask.echobot.repositories.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Optional<TelegramUser> findByTelegramId(Long telegramId) {
        return telegramUserRepository.findById(telegramId);
    }
}
