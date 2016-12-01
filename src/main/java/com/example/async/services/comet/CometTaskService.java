package com.example.async.services.comet;


import com.example.async.models.Task;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * タスクを消化するサービス(Comet版)
 */
public interface CometTaskService {

    /**
     * 非同期にタスクを消化する
     * タスクが終わったら{@link DeferredResultTopic}に通知{@link DeferredResultTopic#publish}する
     *
     * @param id タスクID
     */
    public void executeAllDeferredResultVersion(Long id);

    /**
     * /**
     * 非同期にタスクを消化する
     * タスクが終わったら{@link CompletableFutureTopic}に通知する
     *
     * @param id タスクID
     */
    public void executeAllCompletableFutureVersion(Long id);

    /**
     * タスクの完了を待つ
     *
     * @param id
     */
    public void subscribeDeferredResultVersion(Long id,DeferredResult<Task> deferredResult);

    /**
     * タスクの完了を待つ
     *
     * @param id
     */
    public CompletableFuture subscribeCompletableFutureVersion(Long id);
}
