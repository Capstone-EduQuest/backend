package com.eduquest.backend.domain.reward.service;

import com.eduquest.backend.domain.reward.model.Wallet;

public interface WalletQueryService {
    Wallet findByUserId(Long userId);
}

