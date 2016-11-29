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
 * 更新完了をDeferredResultにPUSHする
 * 更新完了をシステム通してPUSHしたい場合を想定し、
 * Singletonにする(リクエストごとに生成する版がCompletabueFutureTopic)
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
    public void subscribe(Long id, DeferredResult deferredResult) {
        synchronized (map) {
            List<DeferredResult> list = map.getOrDefault(id, new CopyOnWriteArrayList<>());
            list.add(deferredResult);
            map.put(id, list);
            log.info("task id : " + id + "subscribe");
        }
    }

    /**
     * タスクIDで指定されたタスクを通知する
     *
     * @param task
     */
    public void publish(Task task) {
        List<DeferredResult> list;
        synchronized (map) {
            list = map.getOrDefault(task.getId(), new CopyOnWriteArrayList<>());
            map.put(task.getId(), new CopyOnWriteArrayList<>());
        }

        log.info("publish: " + task.toString());
        list.forEach(i -> {
            i.setResult(task);
        });
    }

}
