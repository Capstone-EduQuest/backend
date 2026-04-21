package com.eduquest.backend.domain.reward.service;

import com.eduquest.backend.domain.reward.model.WalletHistory;

public interface WalletCommandService {

    void changeBalance(Long userId, Long amount, String reason);

    Long saveWalletHistory(WalletHistory walletHistory);

}

