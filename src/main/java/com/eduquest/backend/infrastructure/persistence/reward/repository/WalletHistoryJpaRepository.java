package com.eduquest.backend.infrastructure.persistence.reward.repository;

import com.eduquest.backend.infrastructure.persistence.reward.entity.WalletHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletHistoryJpaRepository extends JpaRepository<WalletHistoryEntity, Long> {

}

