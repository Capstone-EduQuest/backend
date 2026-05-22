package com.eduquest.backend.infrastructure.coderunner.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface EvaluationQueueRepository {

    boolean offer(UUID submissionUuid);

    UUID poll();

    UUID poll(long timeout, TimeUnit unit) throws InterruptedException;

    UUID take() throws InterruptedException;

}

