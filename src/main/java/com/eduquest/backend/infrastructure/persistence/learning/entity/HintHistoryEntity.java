package com.eduquest.backend.infrastructure.persistence.learning.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hint_history")
public class HintHistoryEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "hint_id", nullable = false)
    private Long hintId;

    @Builder(access = AccessLevel.PROTECTED)
    public HintHistoryEntity(Long memberId, Long hintId) {
        this.memberId = memberId;
        this.hintId = hintId;
    }

    public static HintHistoryEntity of(Long memberId, Long hintId) {
        return HintHistoryEntity.builder().memberId(memberId).hintId(hintId).build();
    }

}
