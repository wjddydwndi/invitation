package com.invitation.module.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
public class ScheduleConfiguration implements AsyncConfigurer, SchedulingConfigurer {
/**
 * Spring scheduler는 동기적으로 실행.
 * 제 시간 안에 작업이 끝나지 않을 경우, 다음 작업을 진행하지 못하는 경우가 발생
 * @Async 사용시 이전 작업 스케줄러의 작업을 기다리지 않고 자신의 작업을 수행.
 * **/
    //@Value("${spring.schedule.pool-size}")
    // private final int poolSize = 300;
    private final int poolSize = 5;

    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("invitation-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(this.threadPoolTaskScheduler());
    }

    public Executor getAsyncExecutor() {
        return this.threadPoolTaskScheduler();
    }
}
