package com.eduquest.backend.infrastructure.coderunner.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Repository
public class InMemoryEvaluationQueueRepository implements EvaluationQueueRepository {

    private static final BlockingQueue<UUID> QUEUE = new LinkedBlockingQueue<>();

    @Override
    public boolean offer(UUID submissionUuid) {
        return QUEUE.offer(submissionUuid);
    }

    @Override
    public UUID poll() {
        return QUEUE.poll();
    }

    @Override
    public UUID poll(long timeout, TimeUnit unit) throws InterruptedException {
        return QUEUE.poll(timeout, unit);
    }

    @Override
    public UUID take() throws InterruptedException {
        return QUEUE.take();
    }
}

