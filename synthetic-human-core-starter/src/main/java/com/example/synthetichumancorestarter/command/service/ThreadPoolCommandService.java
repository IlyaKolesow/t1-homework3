package com.example.synthetichumancorestarter.command.service;

import com.example.synthetichumancorestarter.command.executor.SynthCommandExecutor;
import com.example.synthetichumancorestarter.command.model.SynthCommand;
import com.example.synthetichumancorestarter.command.properties.CommandConfigProperties;
import com.example.synthetichumancorestarter.command.service.exception.ExecutionQueueIsFullException;
import com.example.synthetichumancorestarter.command.validator.SynthCommandValidator;
import com.example.synthetichumancorestarter.metrics.SyntheticHumanMetricService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ThreadPoolCommandService implements CommandService {

    private final SynthCommandValidator synthCommandValidator;
    private final SynthCommandExecutor synthCommandExecutor;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final CommandConfigProperties commandConfigProperties;
    private final SyntheticHumanMetricService metricService;

    public ThreadPoolCommandService(
            SynthCommandValidator synthCommandValidator,
            SynthCommandExecutor synthCommandExecutor,
            CommandConfigProperties commandConfigProperties,
            SyntheticHumanMetricService metricService) {

        this.synthCommandExecutor = synthCommandExecutor;
        this.synthCommandValidator = synthCommandValidator;
        this.commandConfigProperties = commandConfigProperties;
        this.metricService = metricService;
        final var poolProperties = commandConfigProperties.getPoolProperties();
        this.threadPoolExecutor = new ThreadPoolExecutor(
                poolProperties.getMinSize(),
                poolProperties.getMaxSize(),
                poolProperties.getIdleThreadKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(poolProperties.getQueueCapacity()),
                new BasicThreadFactory.Builder()
                        .namingPattern("CSThread-%d")
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Scheduled(
            initialDelayString = "#{synthHumanProperties.getBusyness().getInitialDelay()}",
            fixedDelayString = "#{synthHumanProperties.getBusyness().getFixedDelay()}")
    public void publishQueueSizeMetric() {
        metricService.publishQueueSizeMetric(threadPoolExecutor.getQueue().size());
    }

    public void processCommand(SynthCommand command) throws ExecutionQueueIsFullException {
        synthCommandValidator.validate(command);
        switch (command.getPriority()) {
            case COMMON -> {
                try {
                    threadPoolExecutor.execute(() -> synthCommandExecutor.execute(command));
                } catch (RejectedExecutionException e) {
                    throw new ExecutionQueueIsFullException("Command [%s] rejected, execution queue is full!".formatted(command), e);
                }
            }
            case CRITICAL -> synthCommandExecutor.execute(command);
        }
    }

    @PreDestroy
    public void preDestroy() {
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(commandConfigProperties.getPoolProperties().getTerminationTimeout().toNanos(),
                    TimeUnit.NANOSECONDS)) {

                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
