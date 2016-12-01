package com.example.async.controllers;

import com.example.async.models.Task;
import com.example.async.services.common.SharedService;
import com.example.async.services.polling.PollingTaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * ポーリング用のコントローラ
 * ポーリングなのでServiceをexecuteしたあとの戻り値は利用しない
 * POINT Spring4.3からはコンストラクタインジェクションを暗黙的にサポートするため@Autowired不要
 */
@RestController
@AllArgsConstructor
@RequestMapping("polling/tasks")
public class PollingController {
    final PollingTaskService pollingTaskService;
    final SharedService sharedService;

    /**
     * タスクをスタートさせる
     *
     * @param quantity
     * @return
     */
    @PostMapping
    public Task start(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = sharedService.register(quantity.orElse(10));
        pollingTaskService.executeAll(task.getId());
        return task;
    }


    /**
     * ポーリングのために現在のタスクの状態を表示する
     * POINT: ポーリングはサーバから見ると通常のHTTPリクエストと変わらない
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Task status(@PathVariable("id") Long id) {
        return sharedService.status(id);
    }
}
