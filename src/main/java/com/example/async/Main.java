package com.example.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final ExecutorService service = Executors.newScheduledThreadPool(5);
        Exchanger<String> changer = new Exchanger<>();

        service.execute(() -> {
            log.info("start");
            sleep(4);
            try {
                String other = changer.exchange(Thread.currentThread().getName() + ":12345");
                log.info(other);
                log.info("change end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("end");
        });

        service.execute(() -> {
            log.info("start");
            try {
                String other = changer.exchange(Thread.currentThread().getName() + ":678910");
                log.info(other);
                log.info("change end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sleep(2);
            log.info("end");
        });

        log.info("main end");
        service.shutdown();
    }

    public static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
