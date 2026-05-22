package com.eduquest.backend.presentation.submission.dto.response;

import java.util.UUID;

public record ValuationResponse(UUID uuid) {

    public static ValuationResponse of(UUID uuid) {
        return new ValuationResponse(uuid);
    }

}

