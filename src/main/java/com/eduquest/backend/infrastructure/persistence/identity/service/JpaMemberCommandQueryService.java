package com.eduquest.backend.infrastructure.persistence.identity.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.model.Member;
import com.eduquest.backend.domain.member.service.MemberCommandQueryService;
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

@Service
@RequiredArgsConstructor
public class JpaMemberCommandQueryService implements MemberCommandQueryService {

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

        return userRoleRepository.save(userRoleEntity).getId();

    }
}
