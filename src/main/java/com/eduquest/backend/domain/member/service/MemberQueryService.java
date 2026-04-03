package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.dto.MemberQuery;

public interface MemberQueryService {

    boolean isExistByEmail(String email);

    MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email);

}
