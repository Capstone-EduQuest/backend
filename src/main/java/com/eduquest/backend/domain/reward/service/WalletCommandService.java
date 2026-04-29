package com.eduquest.backend.domain.reward.service;

public interface WalletCommandService {

    void changeBalance(Long userId, Long amount, String reason);

}

