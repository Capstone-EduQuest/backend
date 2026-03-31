package com.eduquest.backend.infrastructure.persistence.common.config;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BasicUpdateEntity extends BasicEntity {

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

}
