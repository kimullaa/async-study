package com.example.async.services.comet;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CompletableFutureTopicを管理する
 * リクエストをまたがってインスタンス管理するために作ったクラス
 */
@Component
public class CompletableFutureTopicStore {
    Map<Long, CompletableFutureTopic> store = new ConcurrentHashMap<>();


    public void add(CompletableFutureTopic topic) {
        store.put(topic.getId(), topic);
    }

    public void remove(Long id) {
        store.remove(id);
    }

    public synchronized CompletableFutureTopic get(Long id) {
        CompletableFutureTopic topic = store.get(id);
        if (topic == null) {
            throw new RuntimeException("タスクが存在しないかすでに完了しています");
        }
        return topic;
    }

}
