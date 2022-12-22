package com.testtask.echobot.commands;

import com.testtask.echobot.config.BotConfig;
import com.testtask.echobot.models.TelegramUser;
import com.testtask.echobot.services.BotMessageService;
import com.testtask.echobot.services.TelegramUserService;
import com.testtask.echobot.services.UserQueueService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class TextCommand implements Command {
    private final BotMessageService messageService;

    private final TelegramUserService userService;

    private final UserQueueService userQueueService;

    private final BotConfig botConfig;

    ExecutorService executorService = ForkJoinPool.commonPool();

    public TextCommand(BotMessageService messageService, TelegramUserService userService, UserQueueService userQueueService, BotConfig botConfig) {
        this.messageService = messageService;
        this.userService = userService;
        this.userQueueService = userQueueService;
        this.botConfig = botConfig;
    }

    @Override
    public void execute(Update update){
        log.info("User with name {} and id {} sent message: {}", update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getId(), update.getMessage().getText());
        executorService.submit(() -> {
            TelegramUser user = userService.findByTelegramId(update.getMessage().getFrom().getId()).orElse(null);
            user.setRequestCount(user.getRequestCount() + 1);
            userService.save(user);
            userQueueService.submitTask(user.getId(), () -> messageService.sendMessage(update.getMessage().getChatId().toString(), update.getMessage().getText() + " " + user.getRequestCount()));
            try {
                Thread.sleep(botConfig.getEchoDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            userQueueService.invokeTask(user.getId());
        });
    }
}
