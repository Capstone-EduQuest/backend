package com.eduquest.backend.infrastructure.security.service;

import com.eduquest.backend.infrastructure.persistence.identity.repository.MemberQueryRepository;
import com.eduquest.backend.domain.member.dto.MemberQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authz") // SpEL에서 @authz 로 호출 가능
@RequiredArgsConstructor
public class AuthorizationService {

    private final MemberQueryRepository memberQueryRepository;

    // userId 경로를 사용할 때 (GET /users/{userId}/uuid)
    public boolean isSelfByUserId(Authentication authentication, String userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getName().equals(userId);
    }

    // uuid 경로를 사용할 때 (GET/PUT/DELETE /users/{uuid})
    public boolean isSelfByUuid(Authentication authentication, UUID uuid) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object details = authentication.getDetails();

        if (details instanceof String) {
            try {
                UUID userUuid = UUID.fromString((String) details);

                if (userUuid.equals(uuid)) {
                    return true;
                }
            } catch (Exception e) {
                // details가 UUID로 변환할 수 없는 경우, 계속해서 DB 조회로 넘어감
            }
        }

        String authUserId = authentication.getName();
        return memberQueryRepository.findUserProfileByUuid(uuid)
                .map(MemberQuery.UserProfile::userId)
                .map(ownerUserId -> ownerUserId.equals(authUserId))
                .orElse(false);
    }

}

