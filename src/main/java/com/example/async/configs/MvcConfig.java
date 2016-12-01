package com.example.async.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/polling").setViewName("polling");
        registry.addViewController("/comet").setViewName("comet");
        registry.addViewController("/batch").setViewName("batch");
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(getTaskExecutor());
    }

    /**
     * POINT: デフォルトの動作では@Async実行のたびに新スレッドが生成されるためスレッドプールを生成する
     * @return
     */
    @Bean
    public AsyncTaskExecutor getTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(30);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(100);
        return taskExecutor;
    }
}
