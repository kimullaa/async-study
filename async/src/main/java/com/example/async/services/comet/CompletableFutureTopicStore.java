package com.example.async.services.comet;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * idごとにCompletableFutureTopicを管理する
 * リクエストをまたがってインスタンス管理するためのクラス
 */
@Component
public class CompletableFutureTopicStore {
    Map<Long, CompletableFutureTopic> store = new ConcurrentHashMap<>();


    public void add(CompletableFutureTopic topic ){
        store.put(topic.getId(), topic);
    }

    public CompletableFutureTopic get(Long id) {
        return store.get(id);
    }

}
