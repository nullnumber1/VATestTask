package com.testtask.echobot.services;

import com.google.common.collect.ImmutableMap;
import com.testtask.echobot.commands.Command;
import com.testtask.echobot.commands.StartCommand;
import com.testtask.echobot.commands.TextCommand;
import com.testtask.echobot.config.BotConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Data
@Service
public class CommandMapService {
    private final ImmutableMap<String, Command> commandList;

    private final Command echoCommand;

    @Autowired
    public CommandMapService(@Lazy BotMessageService messageService, TelegramUserService userService, UserQueueService userQueueService, BotConfig botConfig) {
        commandList = ImmutableMap.<String, Command>builder()
                .put("/start", new StartCommand(messageService, userService))
                .build();
        echoCommand = new TextCommand(messageService, userService, userQueueService, botConfig);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandList.getOrDefault(commandIdentifier, echoCommand);
    }
}
