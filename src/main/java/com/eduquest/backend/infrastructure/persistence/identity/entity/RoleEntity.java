package com.eduquest.backend.infrastructure.persistence.identity.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role")
public class RoleEntity extends BasicEntity {

    private String name;

    @Builder(access = AccessLevel.PROTECTED)
    public RoleEntity(String name) {
        this.name = name;
    }

    public static RoleEntity of(String name) {
        return new RoleEntity(name);
    }

}
