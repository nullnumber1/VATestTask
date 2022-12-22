package com.testtask.echobot.controllers;

import com.testtask.echobot.dao.EchoMessageDao;
import com.testtask.echobot.dao.UpdateDelayDao;
import com.testtask.echobot.models.TelegramUser;
import com.testtask.echobot.config.BotConfig;
import com.testtask.echobot.services.TelegramUserService;
import com.testtask.echobot.services.UserQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@RestController
@Slf4j
@RequestMapping("/api/v1/")
public class EchoRestControllerV1 {
    private final TelegramUserService userService;
    private final UserQueueService userQueueService;
    ExecutorService executorService = ForkJoinPool.commonPool();
    private final BotConfig botConfig;

    @Autowired
    public EchoRestControllerV1(TelegramUserService userService, UserQueueService userQueueService, BotConfig botConfig) {
        this.userService = userService;
        this.userQueueService = userQueueService;
        this.botConfig = botConfig;
    }

    @PostMapping("/receiveEchoMessage")
    public ResponseEntity<Object> receiveEchoMessage(@RequestBody EchoMessageDao echoMessageDao) {
        if (echoMessageDao.getMessage() == null || echoMessageDao.getUserSender() == null) {
            log.info("Message is non valid:\n{}", echoMessageDao);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TelegramUser user = userService.findByTelegramId(echoMessageDao.getUserSender()).orElse(null);
        user.setRequestCount(user.getRequestCount() + 1);
        userService.save(user);
        try {
            Thread.sleep(botConfig.getEchoDelay());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Map<Object, Object> response = new HashMap<>();
        response.put("is_ok", true);
        response.put("message_number", userService.findByTelegramId(echoMessageDao.getUserSender()).orElse(null).getRequestCount());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateQueueDelay")
    public ResponseEntity<Object> updateQueueDelay(@RequestBody UpdateDelayDao updateDelayDao) {
        if (updateDelayDao.getNewDelay() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        botConfig.setEchoDelay(updateDelayDao.getNewDelay());
        Map<Object, Object> response = new HashMap<>();
        response.put("new_delay", botConfig.getEchoDelay());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
