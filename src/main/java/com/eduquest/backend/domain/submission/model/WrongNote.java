package com.eduquest.backend.domain.submission.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WrongNote {

    private Long id;
    private UUID uuid;
    private Long userId;
    private Long problemId;
    private String wrongAnswer;
    private String aiExplanation;
    private Boolean isReviewed;
    private LocalDateTime nextReviewAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(access = AccessLevel.PROTECTED)
    public WrongNote(Long userId, Long problemId, String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.problemId = problemId;
        this.wrongAnswer = wrongAnswer;
        this.aiExplanation = aiExplanation;
        this.isReviewed = isReviewed;
        this.nextReviewAt = nextReviewAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WrongNote of(Long userId, Long problemId, String wrongAnswer, String aiExplanation) {
        return WrongNote.builder()
                .userId(userId)
                .problemId(problemId)
                .wrongAnswer(wrongAnswer)
                .aiExplanation(aiExplanation)
                .isReviewed(Boolean.FALSE)
                .build();
    }

    public static WrongNote of(UUID uuid, Long id, Long userId, Long problemId, String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        WrongNote wn = WrongNote.of(userId, problemId, wrongAnswer, aiExplanation);
        wn.uuid = uuid;
        wn.id = id;
        wn.isReviewed = isReviewed;
        wn.nextReviewAt = nextReviewAt;
        wn.createdAt = createdAt;
        wn.updatedAt = updatedAt;
        return wn;
    }

    // 오답 업데이트: wrongAnswer/aiExplanation/nextReviewAt 갱신, isReviewed=false로 리셋
    public void updateWrongAnswer(String wrongAnswer, String aiExplanation, LocalDateTime nextReviewAt) {
        this.wrongAnswer = wrongAnswer;
        this.aiExplanation = aiExplanation;
        this.nextReviewAt = nextReviewAt;
        this.isReviewed = Boolean.FALSE;
    }

    // 검토 완료 처리
    public void markReviewed() {
        this.isReviewed = Boolean.TRUE;
        this.nextReviewAt = null;
    }

    public void scheduleNextReview(LocalDateTime when) {
        this.nextReviewAt = when;
    }

}

