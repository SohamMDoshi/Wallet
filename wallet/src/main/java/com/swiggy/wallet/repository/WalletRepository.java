package com.swiggy.wallet.repository;

import com.swiggy.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query(value = "SELECT * FROM public.wallet WHERE id = ?1 AND users_id = ?2", nativeQuery = true)
    Optional<Wallet>findByUserId(Long walletId, Long userID);
    @Query(value = "SELECT * FROM public.wallet WHERE users_id = ?1", nativeQuery = true)
    Set<Wallet> findAllWallets(Long userId);
}
