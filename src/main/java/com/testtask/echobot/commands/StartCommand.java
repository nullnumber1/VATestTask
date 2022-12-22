package com.testtask.echobot.commands;

import com.testtask.echobot.models.TelegramUser;
import com.testtask.echobot.services.BotMessageService;
import com.testtask.echobot.services.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class StartCommand implements Command {

    private final BotMessageService messageService;
    private final TelegramUserService userService;

    public StartCommand(BotMessageService messageService, TelegramUserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }
    @Override
    public void execute(Update update) {
        TelegramUser user;
        log.info("Telegram user id: {}", update.getMessage().getFrom().getId());
        if (userService.findByTelegramId(update.getMessage().getFrom().getId()).isEmpty()) {
            user = new TelegramUser();
            user.setId(update.getMessage().getFrom().getId());
            user.setRequestCount(0L);
            userService.save(user);
            messageService.sendMessage(update.getMessage().getChatId().toString(), "Hello, " + update.getMessage().getFrom().getFirstName() + ". I'm EchoBot. I can repeat your messages.");
            return;
        }
        messageService.sendMessage(update.getMessage().getChatId().toString(), "Hello, " + update.getMessage().getFrom().getFirstName() + ". You've already registered.");
    }
}
