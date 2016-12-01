package com.example.async.services.comet;

import com.example.async.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 処理完了をpub/subするためのクラス
 * すべてのIdに対する更新を受け付けるためにSingletonにする
 */
@Slf4j
@Component
public class DeferredResultTopic {
    Map<Long, List<DeferredResult>> map = new ConcurrentHashMap<>();

    /**
     * 引数で指定されたTaskが更新されたときに通知する
     *
     * @param id 監視するタスク
     * @param deferredResult 通知先
     */
    public synchronized void subscribe(Long id, DeferredResult deferredResult) {
        List<DeferredResult> list = map.getOrDefault(id, new CopyOnWriteArrayList<>());
        list.add(deferredResult);
        map.put(id, list);
        log.info("task id : " + id + "subscribe");
    }

    /**
     * タスクIDで指定されたタスクを通知する
     *
     * @param task
     */
    public synchronized void publish(Task task) {
        List<DeferredResult> list;
        list = map.getOrDefault(task.getId(), new CopyOnWriteArrayList<>());
        map.put(task.getId(), new CopyOnWriteArrayList<>());

        log.info("publish: " + task.toString());
        list.forEach(i -> {
            i.setResult(task);
        });
    }

}
