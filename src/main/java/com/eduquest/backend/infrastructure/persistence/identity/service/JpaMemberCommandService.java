package com.eduquest.backend.infrastructure.persistence.identity.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.domain.identity.model.Role;
import com.eduquest.backend.domain.identity.service.MemberCommandService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.identity.entity.MemberEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.RoleEntity;
import com.eduquest.backend.infrastructure.persistence.identity.entity.UserRoleEntity;
import com.eduquest.backend.infrastructure.persistence.identity.mapper.MemberMapper;
import com.eduquest.backend.infrastructure.persistence.identity.mapper.RoleMapper;
import com.eduquest.backend.infrastructure.persistence.identity.repository.MemberJpaRepository;
import com.eduquest.backend.infrastructure.persistence.identity.repository.RoleJpaRepository;
import com.eduquest.backend.infrastructure.persistence.identity.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaMemberCommandService implements MemberCommandService {

    private final MemberJpaRepository memberJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserRoleJpaRepository userRoleRepository;
    private final MemberMapper memberMapper;
    private final RoleMapper roleMapper;

    @Transactional
    @Override
    public Long saveMember(Member member, String role) {

        if (memberJpaRepository.existsByEmail(member.getEmail()) || memberJpaRepository.existsByUserId(member.getUserId())) {
            throw new EduQuestException(DataBaseErrorCode.ALREADY_EXIST_MEMBER);
        }

        MemberEntity memberEntity = memberMapper.toEntity(member);

        memberEntity = memberJpaRepository.save(memberEntity);

        RoleEntity roleEntity = roleJpaRepository.findByName(role)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));

        UserRoleEntity userRoleEntity = UserRoleEntity.of(memberEntity, roleEntity);

        userRoleRepository.save(userRoleEntity);

        return memberEntity.getId();

    }

    @Override
    public Long saveRole(Role role) {
        return roleJpaRepository.save(roleMapper.toEntity(role)).getId();
    }

    @Transactional
    @Override
    public Long updateMember(Member member) {

        if (!memberJpaRepository.existsById(member.getId())) {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

        MemberEntity memberEntity = memberMapper.toEntity(member);

        return memberJpaRepository.save(memberEntity).getId();

    }

    @Transactional
    @Override
    public Long updateUserRole(Long id, Long roleId) {

        if (!roleJpaRepository.existsById(roleId)) {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

        UserRoleEntity userRoleEntity = userRoleRepository.findByMemberId(id)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));

        RoleEntity roleEntity = roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA));

        userRoleEntity.updateRole(roleEntity);

        return userRoleRepository.save(userRoleEntity).getId();
    }

    @Transactional
    @Override
    public void deleteMember(UUID uuid) {

        if (!memberJpaRepository.existsByUuid(uuid)) {
            throw new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA);
        }

        memberJpaRepository.deleteByUuid(uuid);
    }
}
