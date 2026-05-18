package com.eduquest.backend.domain.submission.service;

import java.util.UUID;

public interface EvaluationWorkerService {

    void processSingle();

    void processWithRetry(UUID submissionUuid);

}
