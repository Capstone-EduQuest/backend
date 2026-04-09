package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.learning.model.Stage;

import java.util.UUID;

public interface StageCommandService {

    Long saveStage(Stage stage);

    Long updateStage(Stage stage);

    void deleteStage(UUID uuid);

}

