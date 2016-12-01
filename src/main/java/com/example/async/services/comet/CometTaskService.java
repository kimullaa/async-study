package com.example.async.services.comet;


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

}
