package com.testtask.echobot.services;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Data
public class UserQueueService {
    private HashMap<Long, BlockingQueue<Runnable>> taskQueueMap;

    @PostConstruct
    private void init() {
        taskQueueMap = new HashMap<>();
    }

    public void addQueue(Long userId) {
        taskQueueMap.put(userId, new LinkedBlockingQueue<>());
    }


    public synchronized void submitTask(Long userId, Runnable task) {
        if (!taskQueueMap.containsKey(userId)) {
            addQueue(userId);
        }
        taskQueueMap.get(userId).add(task);
    }

    public synchronized void invokeTask(Long userId) {
        taskQueueMap.get(userId).poll().run();
    }
}
