package com.example.async.services.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE用のサービスクラス
 */
public interface SseService {
    /**
     * 非同期にタスクを消化する
     * タスクが終わるたびに{@link SseEmitter}に書き込みを行う
     *
     * @param id
     * @param emitter
     */
    public void executeAll(Long id, SseEmitter emitter);
}
