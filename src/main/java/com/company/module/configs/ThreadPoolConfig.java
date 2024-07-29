package com.company.module.configs;

import com.company.module.configs.context.ContextHolder;
import com.company.module.common.Constants;
import com.company.module.configs.properties.ThreadPoolProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.RejectedExecutionHandler;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final ThreadPoolProperties threadPoolProperties;
    private final TaskSchedulingProperties taskSchedulingProperties;

    @Bean(name = Constants.Executor.EXECUTOR_ASYNC)
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = this.builder(threadPoolProperties.getExecutor().getAsyncTask());
        executor.initialize();
        return executor;
    }

    @Bean(name = Constants.Executor.EXECUTOR_SCHEDULER)
    public TaskScheduler schedulerExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(taskSchedulingProperties.getPool().getSize());
        threadPoolTaskScheduler.setThreadNamePrefix("schedulerTPool-");
        return threadPoolTaskScheduler;
    }

    @Bean(name = Constants.Executor.EXECUTOR_MAIN)
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return this.builder(threadPoolProperties);
    }

    private ThreadPoolTaskExecutor builder(ThreadPoolProperties property) {
        return new TaskExecutorBuilder()
                .taskDecorator(decorator)
                .corePoolSize(property.getCorePoolSize())
                .maxPoolSize(property.getMaxPoolSize())
                .queueCapacity(property.getQueueCapacity())
                .threadNamePrefix(property.getThreadNamePrefix())
                .customizers(taskExecutor -> taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler))
                .build();
    }

    private final TaskDecorator decorator = runnable -> {
        var logContext = MDC.getCopyOfContextMap();
        var context = ContextHolder.getContext();
        return () -> {
            try {
                ContextHolder.setContext(context);
                MDC.setContextMap(logContext);
                runnable.run();
            } finally {
                ContextHolder.clearContext();
                MDC.clear();
            }
        };
    };

    private final RejectedExecutionHandler rejectedExecutionHandler = (runnable, executor) -> {
        if (executor.isShutdown()) {
            return;
        }
        var queue = executor.getQueue();
        try {
            queue.put(runnable);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };
}
