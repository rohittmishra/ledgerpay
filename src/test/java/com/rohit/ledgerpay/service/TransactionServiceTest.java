package com.rohit.ledgerpay.service;

import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.Transaction;
import com.rohit.ledgerpay.repository.AccountRepository;
import com.rohit.ledgerpay.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(new BigDecimal("1000.00"));

        toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("500.00"));
    }

    @Test
    void transferMoney_shouldSucceed_whenBalanceIsSufficient() {
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.transferMoney(1L, 2L, new BigDecimal("300.00"));

        assertEquals("SUCCESS", result.getStatus());
        assertEquals(new BigDecimal("700.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("800.00"), toAccount.getBalance());
        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }

    @Test
    void transferMoney_shouldThrowException_whenBalanceIsInsufficient() {
        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> transactionService.transferMoney(1L, 2L, new BigDecimal("5000.00")));

        assertTrue(exception.getMessage().contains("Insufficient balance"));
        assertEquals(new BigDecimal("1000.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), toAccount.getBalance());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void transferMoney_shouldThrowException_whenSameAccountIdsGiven() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.transferMoney(1L, 1L, new BigDecimal("100.00")));

        assertEquals("Cannot transfer to the same account", exception.getMessage());
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }
}