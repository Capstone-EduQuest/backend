package com.eduquest.backend.infrastructure.persistence.identity.entity;

import com.eduquest.backend.infrastructure.persistence.common.entity.BasicUpdateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class MemberEntity extends BasicUpdateEntity {

    private String userId;

    private String email;

    private String password;

    private LocalDate birth;

    private String nickname;

    private Boolean isLocked;

    private Long profileId;

    @Builder(access = AccessLevel.PROTECTED)
    public MemberEntity(String userId, String email, String password, LocalDate birth, String nickname, Boolean isLocked, Long profileId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.nickname = nickname;
        this.isLocked = isLocked;
        this.profileId = profileId;
    }

    public static MemberEntity of(String userId, String email, String password, LocalDate birth, String nickname, Boolean isLocked, Long profileId) {
        return MemberEntity.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .birth(birth)
                .nickname(nickname)
                .isLocked(isLocked != null ? isLocked : false)
                .profileId(profileId)
                .build();
    }

}
