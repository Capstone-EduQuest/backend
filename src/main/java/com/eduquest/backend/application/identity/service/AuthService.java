package com.eduquest.backend.application.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher eventPublisher;

    public void findIdByEmail(String email) {



    }

}
