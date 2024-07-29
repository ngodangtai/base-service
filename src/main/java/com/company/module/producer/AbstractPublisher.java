package com.company.module.producer;

import com.company.module.common.Constants;
import com.company.module.configs.context.ContextHolder;
import com.company.module.event.dto.base.*;
import com.company.module.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractPublisher<T extends BaseEvent<?>> implements Publisher {

    @Async(Constants.Executor.EXECUTOR_ASYNC)
    @Retryable(retryFor = {Exception.class}, maxAttempts = 4, backoff = @Backoff(delay = 30000L))
    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public void publish(BaseEvent<?> baseEvent) {
        try {
            log.info("Publish actionType: {}, event: {}", baseEvent.getBody().getActionType(), MapperUtils.writeValueAsString(baseEvent));
            ContextHolder.setContext(baseEvent.getContext());
            send((T) baseEvent);
        } catch (Exception ex) {
            log.error("Publish event: {} - error: {}", MapperUtils.writeValueAsString(baseEvent), ex.getMessage(), ex);
            throw ex;
        }
    }

    protected abstract void send(T baseEvent);
}
