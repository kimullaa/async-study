package com.example.async.services.comet;


/**
 * タスクを消化するサービス(Comet版)
 * 通知を受けたいDeferredResultがTopicに格納されているため
 * Topicのsubscribeを実施する
 */
public interface CometTaskService {

    /**
     * 非同期にタスクを消化する
     * タスクは1sに1つ消化される
     * タスクが終わったらTopicを利用して通知する
     *
     * @param id タスクID
     */
    public void executeAllDeferredResultVersion(Long id);
    public void executeAllCompletableFutureVersion(Long id);

}
