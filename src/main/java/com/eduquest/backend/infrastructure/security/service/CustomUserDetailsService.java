package com.eduquest.backend.infrastructure.security.service;

import com.eduquest.backend.common.exception.EduQuestException;
import com.eduquest.backend.domain.member.dto.UserDetailsData;
import com.eduquest.backend.infrastructure.persistence.common.exception.DataBaseErrorCode;
import com.eduquest.backend.infrastructure.persistence.identity.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetailsData userDetailsData = memberQueryRepository.findUserDetailsByUserId(username)
                .orElseThrow(() -> new EduQuestException(DataBaseErrorCode.NOT_FOUND_DATA, new HashMap<>(
                        Map.of("userId", username)
                )));

        return User.builder()
                .username(userDetailsData.id())
                .password(userDetailsData.password())
                .roles(userDetailsData.role())
                .accountLocked(userDetailsData.isLocked())
                .build();
    }
}
