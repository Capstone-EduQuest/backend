package com.eduquest.backend.domain.submission.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {

    private Long id;
    private UUID uuid;
    private Long userId;
    private Long problemId;
    private String answer;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Submission(Long userId, Long problemId, String answer) {
        this.userId = userId;
        this.problemId = problemId;
        this.answer = answer;
    }

    public static Submission of(Long userId, Long problemId, String answer) {
        return Submission.builder()
                .userId(userId)
                .problemId(problemId)
                .answer(answer)
                .build();
    }

    public static Submission of(UUID uuid, Long id, Long userId, Long problemId, String answer, LocalDateTime createdAt) {
        Submission s = Submission.of(userId, problemId, answer);
        s.uuid = uuid;
        s.id = id;
        s.createdAt = createdAt;
        return s;
    }

}

