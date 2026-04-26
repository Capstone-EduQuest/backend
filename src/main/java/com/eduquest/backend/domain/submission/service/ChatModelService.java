package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.dto.AiFeedBackRequest;

public interface ChatModelService {

    String generateAiExplanation(AiFeedBackRequest request);

}
