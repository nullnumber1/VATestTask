package com.testtask.echobot.bot;

import com.testtask.echobot.services.CommandMapService;
import com.testtask.echobot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EchoBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CommandMapService commandMapService;

    @Autowired
    public EchoBot(BotConfig botConfig, CommandMapService commandMapService) {
        this.botConfig = botConfig;
        this.commandMapService = commandMapService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String commandIdentifier = update.getMessage().getText();
            commandMapService.retrieveCommand(commandIdentifier).execute(update);
        }
    }
}