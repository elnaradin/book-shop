package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.payment.transaction.TransactionDto;
import com.example.mybookshopapp.model.payments.BalanceTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {
    @Query("Select new com.example.mybookshopapp.dto.payment.transaction.TransactionDto(bt.time, bt.value, bt.description) " +
            "from BalanceTransactionEntity bt " +
            "where bt.user.email like ?1")
    Page<TransactionDto> findTransactionsByUserEmail(String userName, Pageable pageable);
}
