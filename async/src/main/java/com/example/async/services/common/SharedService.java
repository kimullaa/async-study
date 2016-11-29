package com.example.async.services.common;

import com.example.async.models.Task;

public interface SharedService {
    /**
     * タスクを登録する
     *
     * @param total
     * @return タスクIDを返す
     */
    public Task register(int total);

    /**
     * タスクの状態を返す
     *
     * @param id タスクID
     * @return タスク
     */
    public Task status(Long id);
}
