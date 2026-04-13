package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.dto.request.CodeEvaluateRequest;
import com.eduquest.backend.domain.submission.dto.response.CodeEvaluateResponse;

public interface CodeRunnerService {

    CodeEvaluateResponse evaluate(CodeEvaluateRequest codeEvaluateRequest);

}
