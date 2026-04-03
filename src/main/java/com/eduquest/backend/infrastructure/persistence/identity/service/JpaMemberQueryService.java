package com.eduquest.backend.infrastructure.persistence.identity.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.service.MemberQueryService;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.identity.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JpaMemberQueryService implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public boolean isExistByEmail(String email) {
        return memberQueryRepository.existsByEmail(email);
    }

    @Override
    public MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email) {
        return memberQueryRepository.findEmailAndUserIdByEmail(email)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA,
                        new HashMap<>() {{
                            put("email", "이메일을 찾을 수 없습니다.");
                        }}));
    }

}
