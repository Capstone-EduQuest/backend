package com.eduquest.backend.infrastructure.persistence.identity.repository;

import com.eduquest.backend.infrastructure.persistence.identity.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findById(Long id);

    Optional<RoleEntity> findByName(String name);

}
