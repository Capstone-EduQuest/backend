package com.eduquest.backend.presentation.learning.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

public record StageListRequest(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "30") int size,
        @RequestParam(defaultValue = "created_at") String sort,
        @RequestParam(name = "is_asc", defaultValue = "true") Boolean isAsc
) {

}

