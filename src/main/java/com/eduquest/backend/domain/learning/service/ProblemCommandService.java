package com.eduquest.backend.domain.learning.service;

import com.eduquest.backend.domain.learning.model.Problem;

import java.util.UUID;

public interface ProblemCommandService {

    Long saveProblem(Problem problem);

    Long updateProblem(Problem problem);

    void deleteProblem(UUID uuid);

}

