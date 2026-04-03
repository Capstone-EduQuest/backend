package com.eduquest.backend.domain.member.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberQuery {

    public record EmailAndUserId(String email, String userId) {

        public static EmailAndUserId of(String email, String userId) {
            return new EmailAndUserId(email, userId);
        }

    }

}
