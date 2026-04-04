package com.eduquest.backend.domain.member.service;

import com.eduquest.backend.domain.member.dto.MemberQuery;
import com.eduquest.backend.domain.member.model.Member;

public interface MemberQueryService {

    boolean isExistByEmail(String email);

    Member findMemberByEmail(String email);

    MemberQuery.EmailAndUserId findEmailAndUserIdByEmail(String email);

}
