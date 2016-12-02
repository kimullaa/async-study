package com.example.async.controllers;

import com.example.async.models.Task;
import com.example.async.services.common.SharedService;
import com.example.async.services.sse.SseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

/**
 * SSE用のコントローラ
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("sse")
public class SseController {
    final SharedService asyncSharedService;
    final SseService sseService;

    @PostMapping
    public SseEmitter start(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));

        SseEmitter emitter = new SseEmitter();
        sseService.executeAll(task.getId(),emitter);
        return emitter;
    }
}
