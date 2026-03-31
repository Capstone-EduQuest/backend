package com.eduquest.backend.infrastructure.persistence.identity.mapper;

import com.eduquest.backend.domain.member.model.Role;
import com.eduquest.backend.infrastructure.persistence.identity.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    
    public RoleEntity toEntity(Role role) {
        if (role == null) {
            return null;
        }

        return RoleEntity.of(role.getName());
    }
    
    public Role toDomain(RoleEntity roleEntity) {
        if (roleEntity == null) {
            return null;
        }

        return Role.of(roleEntity.getName());
    }
    
}
