package com.example.async.services.polling;

public interface PollingTaskService {
    /**
     * 非同期にタスクを消化する
     * タスクは1sに1つ消化される
     * 戻り値を利用したい場合はFutureを返す
     * @param id タスクID
     */
    public void executeAll(Long id);
}
