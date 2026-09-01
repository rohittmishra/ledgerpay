package com.rohit.ledgerpay.service;

import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.Transaction;
import com.rohit.ledgerpay.repository.AccountRepository;
import com.rohit.ledgerpay.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Long firstLockId = fromAccountId < toAccountId ? fromAccountId : toAccountId;
        Long secondLockId = fromAccountId < toAccountId ? toAccountId : fromAccountId;

        Account firstLocked = accountRepository.findByIdForUpdate(firstLockId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + firstLockId));
        Account secondLocked = accountRepository.findByIdForUpdate(secondLockId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + secondLockId));

        Account fromAccount = firstLockId.equals(fromAccountId) ? firstLocked : secondLocked;
        Account toAccount = firstLockId.equals(toAccountId) ? firstLocked : secondLocked;

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance in account " + fromAccount.getAccountNumber());
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType("TRANSFER");
        transaction.setStatus("PENDING");
        transactionRepository.save(transaction);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        Transaction transaction = new Transaction();
        transaction.setFromAccount(null);
        transaction.setToAccount(account);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setStatus("PENDING");
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);

        return transaction;
    }
}