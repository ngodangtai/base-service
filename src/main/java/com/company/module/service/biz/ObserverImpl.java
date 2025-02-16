package com.company.module.service.biz;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

public class ObserverImpl {

    private static final Map<String, CompletableFuture<String>> COMPLETABLE_FUTURE_MAP = new ConcurrentHashMap<>();
    private static final long TIMEOUT_DURATION = 10L;

    public String waitTransactionId(String transactionId) {
        if (checkDB(transactionId)) return transactionId;
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            CompletableFuture<String> future = COMPLETABLE_FUTURE_MAP.computeIfAbsent(transactionId, key -> new CompletableFuture<>());
            var task = scope.fork(() -> future.orTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS).join());
            scope.join();
            scope.throwIfFailed();
            return Optional.ofNullable(task.get()).orElse(handleTimeout(transactionId));
        } catch (Exception e) {
            return handleTimeout(transactionId);
        } finally {
            COMPLETABLE_FUTURE_MAP.remove(transactionId);
        }
    }

    private String handleTimeout(String transactionId) {
        if (checkDB(transactionId)) return transactionId;
        // Xử lý nếu timeout xảy ra, call API get HPV
        return null;
    }

    public void fin(String transactionId) {
        saveDB(transactionId);
        publishEvent(transactionId);
        notifyRedis(transactionId);
    }

    public void notifyRedis(String transactionId) {

    }

    public void handleSuccess(String transactionId) {
        Optional.ofNullable(COMPLETABLE_FUTURE_MAP.get(transactionId))
                .ifPresent(future -> future.completeAsync(() -> transactionId));
    }

    private void saveDB(String transactionId) {

    }

    private boolean checkDB(String transactionId) {
        return false; // Xử lý kiểm tra xem đã lưu vào DB chưa
    }

    private void publishEvent(String transactionId) {

    }
}