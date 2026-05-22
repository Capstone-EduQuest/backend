package com.eduquest.backend.infrastructure.persistence.identity.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.dto.MemberQuery;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.service.MemberQueryService;
import com.eduquest.backend.infrastructure.persistence.identity.exception.IdentityDatabaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.identity.mapper.MemberMapper;
import com.eduquest.backend.infrastructure.persistence.identity.repository.MemberQueryRepository;
import com.eduquest.backend.infrastructure.persistence.identity.repository.RoleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaMemberQueryService implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberMapper memberMapper;
    private final RoleQueryRepository roleQueryRepository;

    @Override
    public boolean isExistById(Long id) {
        return memberQueryRepository.existsById(id);
    }

    @Override
    public boolean isExistByEmail(String email) {
        return memberQueryRepository.existsByEmail(email);
    }

    @Override
    public boolean isExistByUserId(String userId) {
        return memberQueryRepository.existsByUserId(userId);
    }

    @Override
    public Member findMemberById(Long id) {
        return memberMapper.toDomain(memberQueryRepository.findById(id)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public Member findMemberByUuid(UUID uuid) {
        return memberMapper.toDomain(memberQueryRepository.findByUuid(uuid)
        .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberMapper.toDomain(memberQueryRepository.findByEmail(email)
        .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email) {
        return memberQueryRepository.findEmailAndUserIdByEmail(email)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND,
                        new HashMap<>() {{
                            put("email", "이메일을 찾을 수 없습니다.");
                        }}));
    }

    @Override
    public UUID findMemberUuidByUserId(String userId) {
        return memberQueryRepository.findUuidByUserId(userId)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND,
                        new HashMap<>() {{
                            put("userId", "유저 아이디를 찾을 수 없습니다.");
                        }}));
    }

    @Override
    public Map<Long, UUID> findMemberUuidByUserIds(List<Long> userIds) {
        return memberQueryRepository.findUuidByUserIds(userIds);
    }

    @Override
    public Long findMemberIdByUuid(UUID uuid) {
        return memberQueryRepository.findIdByUuid(uuid)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Long findMemberIdByUserId(String userId) {
        return memberQueryRepository.findIdByUserId(userId)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND,
                        new HashMap<>() {{
                            put("userId", "유저 아이디를 찾을 수 없습니다.");
                        }}));
    }

    @Override
    public Long findRoleIdByUuid(UUID uuid) {
        return roleQueryRepository.findIdByUuid(uuid)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public MemberQuery.UserProfile findUserProfileByUuid(UUID uuid) {
        return memberQueryRepository.findUserProfileByUuid(uuid)
                .orElseThrow(() -> new EduQuestException(IdentityDatabaseErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public List<MemberQuery.UserListResult> findAllMembersByPagination(int page, int size, String sortBy, String sortDirection) {
        return memberQueryRepository.findAllMembersByPagination(page, size, sortBy, sortDirection);
    }

    @Override
    public List<MemberQuery.Role> findAllRoles() {
        return roleQueryRepository.findAllRoles();
    }

}
