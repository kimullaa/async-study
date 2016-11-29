package com.example.async.services.batch;

import com.example.async.models.Task;

import java.util.concurrent.CompletableFuture;

public interface BatchService {

    /**
     * バッチ処理的に一括登録する
     * 全件登録完了したらCompletableFutureを返す
     * @param id
     * @return
     */
    CompletableFuture<Task> batchExecute(Long id);

}
