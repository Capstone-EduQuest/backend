package com.eduquest.backend.domain.identity.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    private Long id;
    private UUID uuid;
    private String name;
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public Role(String name) {
        this.name = name;
    }

    public static Role of(String name) {
        return Role.builder()
                .name(name)
                .build();
    }

}
