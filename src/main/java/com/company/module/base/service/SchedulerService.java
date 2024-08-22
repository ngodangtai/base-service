/*
 * method should typically have a void return type (if not, the returned value will be ignored)
 * method should not expect any parameters
 *
 * https://www.baeldung.com/spring-scheduled-tasks
 */
package com.company.module.base.service;

import com.company.module.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerService {

    /*
     * Schedule a Task at Fixed Delay
     */
    @Scheduled(fixedDelay = 5000)
//    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void scheduleFixedDelayTask() {
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
    }

    /*
     * Schedule a Task at a Fixed Rate
     */
    @Scheduled(fixedRate = 7000)
//    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void scheduleFixedRateTask() {
        log.info("Fixed rate task - {}", System.currentTimeMillis() / 1000);
    }

    /*
     * This asynchronous task will be invoked each second, even if the previous task isn’t done.
     */
    @Async(Constants.Executor.EXECUTOR_SCHEDULER)
    @Scheduled(fixedRate = 4000)
    public void scheduleFixedRateTaskAsync() {
        log.info("Fixed rate task async - {}", System.currentTimeMillis() / 1000);
        sleep(7000);
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    /*
     * Schedule a Task With Initial Delay (in milliseconds)
     */
    @Scheduled(fixedDelay = 6000, initialDelay = 3000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Fixed rate task with one second initial delay - {}", now);
    }

    /*
     * Schedule a Task Using Cron Expressions
     * https://stackoverflow.com/questions/26147044/spring-cron-expression-for-every-day-101am
     * https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
     */
    @Scheduled(cron = "0 15 10 15 * ?")
//    @Scheduled(cron = "${cron.expression}")
    public void scheduleTaskUsingCronExpression() {
        long now = System.currentTimeMillis() / 1000;
        log.info("schedule tasks using cron jobs - {}", now);
    }
}
