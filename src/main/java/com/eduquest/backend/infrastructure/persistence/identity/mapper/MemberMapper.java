package com.eduquest.backend.infrastructure.persistence.identity.mapper;
import com.eduquest.backend.domain.identity.model.Member;
import com.eduquest.backend.infrastructure.persistence.identity.entity.MemberEntity;
import org.springframework.stereotype.Component;


@Component
public class MemberMapper {
    
    public MemberEntity toEntity(Member member) {
        if (member == null) {
            return null;
        }

        // Use the public factory method to create the entity (builder has protected access)
        return MemberEntity.of(
                member.getUserId(),
                member.getEmail(),
                member.getPassword(),
                member.getBirth(),
                member.getNickname(),
                member.getIsLocked(),
                member.getProfileId()
        );
    }

    public Member toDomain(MemberEntity memberEntity) {
        if (memberEntity == null) {
            return null;
        }

        // Use domain factory 'of' to create Member (excluding persistence metadata)
        return Member.of(
                memberEntity.getUserId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getBirth(),
                memberEntity.getNickname(),
                memberEntity.getIsLocked(),
                memberEntity.getProfileId()
        );

    }

}

