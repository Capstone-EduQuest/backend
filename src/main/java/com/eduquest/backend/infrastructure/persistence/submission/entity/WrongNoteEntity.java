package com.eduquest.backend.infrastructure.persistence.submission.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wrong_note")
public class WrongNoteEntity extends BasicUpdateEntity {

    @Column(name = "wrong_answer", nullable = false, columnDefinition = "TEXT")
    private String wrongAnswer;

    @Column(name = "ai_explanation", columnDefinition = "TEXT")
    private String aiExplanation;

    @Column(name = "is_reviewed", nullable = false)
    private Boolean isReviewed;

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(access = AccessLevel.PROTECTED)
    public WrongNoteEntity(String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, Long userId) {
        this.wrongAnswer = wrongAnswer;
        this.aiExplanation = aiExplanation;
        this.isReviewed = isReviewed;
        this.nextReviewAt = nextReviewAt;
        this.userId = userId;
    }

    public static WrongNoteEntity of(String wrongAnswer, String aiExplanation, Boolean isReviewed, LocalDateTime nextReviewAt, Long userId) {
        return WrongNoteEntity.builder()
                .wrongAnswer(wrongAnswer)
                .aiExplanation(aiExplanation)
                .isReviewed(isReviewed)
                .nextReviewAt(nextReviewAt)
                .userId(userId)
                .build();
    }

    public void markReviewed() {
        this.isReviewed = Boolean.TRUE;
        this.nextReviewAt = null;
    }

    public void scheduleNextReview(LocalDateTime when) {
        this.nextReviewAt = when;
    }

}

