package com.example.async.services.comet;

import com.example.async.models.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 処理完了をpub/subするためのクラス
 * Topicはtaskごとに生成する
 */
@Slf4j
public class CompletableFutureTopic {
    private Task task;
    private final Long id;
    private Executor executor = Executors.newFixedThreadPool(100);
    private CountDownLatch latch = new CountDownLatch(1);

    public CompletableFutureTopic(Long id) {
        this.id = id;
    }

    /**
     * Taskが更新されるまでスレッドを止めて待つ
     * {@link CompletableFutureTopic#publish}が呼ばれると)Futureを返す
     */
    public CompletableFuture subscribe() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(task);
    }

    /**
     * タスクIDで指定されたタスクを通知する
     */
    public synchronized void publish(Task task) {
        this.task = task;
        latch.countDown();
        log.info("publish: " + task.toString());
    }

    public Long getId(){
        return id;
    }

}
