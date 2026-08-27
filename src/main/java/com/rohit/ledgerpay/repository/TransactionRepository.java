package com.rohit.ledgerpay.repository;

import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccount(Account fromAccount);

    List<Transaction> findByToAccount(Account toAccount);

    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);
}
