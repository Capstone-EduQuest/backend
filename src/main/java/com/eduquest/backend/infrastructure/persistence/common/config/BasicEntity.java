package com.eduquest.backend.infrastructure.persistence.common.config;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BasicEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    protected UUID uuid = UUID.randomUUID();

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

}
