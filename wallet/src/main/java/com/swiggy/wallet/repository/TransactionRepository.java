package com.swiggy.wallet.repository;

import com.swiggy.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query(value = "SELECT th.* " +
            "FROM public.transaction th " +
            "WHERE th.user_id = ?1 " +
            "AND th.date_time >= ?2 " +
            "AND th.date_time <= ?3", nativeQuery = true)
    List<Transaction> findTransactionsByUserIdAndTimestamp(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT th.* FROM public.transaction th WHERE th.user_id = ?1", nativeQuery = true)
    List<Transaction> findByCurrentUser(Long userId);


//    SELECT th.*
//    FROM public.transaction_history th
//    JOIN public.users_transaction_histories uth ON th.id = uth.transaction_histories_id
//    WHERE uth.users_id = 1 -- Replace 1 with the desired userId
//    AND th.date_time >= '2024-02-19T08:15:46.590762' -- Start timestamp
//    AND th.date_time <= '2024-02-19T08:15:49.590762'; -- End timestamp
}
