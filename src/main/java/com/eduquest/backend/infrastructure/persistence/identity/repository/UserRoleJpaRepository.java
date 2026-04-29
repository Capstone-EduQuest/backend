package com.eduquest.backend.infrastructure.persistence.identity.repository;

import com.eduquest.backend.infrastructure.persistence.identity.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findByMemberId(Long memberId);

}
