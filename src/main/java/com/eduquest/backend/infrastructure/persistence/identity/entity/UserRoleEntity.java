package com.eduquest.backend.infrastructure.persistence.identity.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role")
public class UserRoleEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false)
    protected Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "role_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RoleEntity role;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    public UserRoleEntity(MemberEntity member, RoleEntity role) {
        this.member = member;
        this.role = role;
    }

    public static UserRoleEntity of(MemberEntity member, RoleEntity role) {
        return UserRoleEntity.builder()
                .member(member)
                .role(role)
                .build();
    }

    public void updateRole(RoleEntity role) {
        this.role = role;
    }

}
