package com.example.async.services.polling;

public interface PollingTaskService {
    /**
     * 非同期にタスクを消化する
     * @param id タスクID
     */
    public void executeAll(Long id);
}
