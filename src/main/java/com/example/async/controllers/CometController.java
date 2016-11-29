package com.example.async.controllers;

import com.example.async.models.Task;
import com.example.async.services.comet.CometTaskService;
import com.example.async.services.comet.CompletableFutureTopicStore;
import com.example.async.services.comet.DeferredResultTopic;
import com.example.async.services.common.SharedService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * comet用のコントローラ
 * SpringMVC外のスレッドで実行された値でも
 * DeferredResultにsetするとその値をレスポンスに返せる
 */
@RestController
@RequestMapping("comet")
@AllArgsConstructor
public class CometController {
    final CometTaskService cometTaskService;
    final SharedService asyncSharedService;
    final DeferredResultTopic deferredResultTopic;
    final CompletableFutureTopicStore store;

    /**
     * タスクをスタートする
     * @param quantity
     * @return
     */
    @PostMapping("deferred-result/tasks")
    public Task startDeferredResult(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));

        cometTaskService.executeAllDeferredResultVersion(task.getId());
        return task;
    }


    /**
     * 現在のタスクの状態を表示する(Comet版)
     * FIXME: 存在しないタスクIDや完了済みのタスクIDを指定すると延々待つ
     *
     * @param id
     * @return
     */
    @GetMapping("deferred-result/tasks/{id}")
    public DeferredResult<Task> statusDeferredResult(@PathVariable("id") Long id) {
        final DeferredResult<Task> deferredResult = new DeferredResult<Task>();
         deferredResultTopic.subscribe(id,deferredResult);
        return deferredResult;
    }

    /**
     * タスクをスタートする
     * @param quantity
     * @return
     */
    @PostMapping("completable-future/tasks")
    public Task startCompletableFuture(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));

        cometTaskService.executeAllCompletableFutureVersion(task.getId());
        return task;
    }


    /**
     * 現在のタスクの状態を表示する(Comet版)
     * FIXME: 存在しないタスクIDや完了済みのタスクIDを指定すると延々待つ
     *
     * @param id
     * @return
     */
    @GetMapping("completable-future/tasks/{id}")
    public CompletableFuture<Task> statusCompletableFuture(@PathVariable("id") Long id) {
        return store.get(id).subscribe();
    }

}
