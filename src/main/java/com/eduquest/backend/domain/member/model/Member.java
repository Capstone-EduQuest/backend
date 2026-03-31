package com.eduquest.backend.domain.member.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private Long id;
    private UUID uuid;
    private String userId;
    private String email;
    private String password;
    private LocalDate birth;
    private String nickname;
    private Boolean isLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long profileId;

    @Builder(access = AccessLevel.PROTECTED)
    public Member(String userId, String email, String password, LocalDate birth, String nickname, Boolean isLocked, Long profileId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.nickname = nickname;
        this.isLocked = isLocked;
        this.profileId = profileId;
    }

    public static Member of(String userId, String email, String password, LocalDate birth, String nickname, Boolean isLocked, Long profileId) {
        return Member.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .birth(birth)
                .nickname(nickname)
                .isLocked(isLocked)
                .profileId(profileId)
                .build();
    }

}
