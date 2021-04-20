package com.anadimisra.throttler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppQueue {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    public void sheduleTask(Runnable task, long initialDelay, long delay) {
        executor.scheduleAtFixedRate(task, initialDelay, delay, TimeUnit.SECONDS);
    }
}
