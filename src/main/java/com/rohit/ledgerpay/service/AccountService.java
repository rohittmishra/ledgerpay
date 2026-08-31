package com.rohit.ledgerpay.service;

import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account openAccount(User user, String accountType) {

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(User user) {
        return accountRepository.findByUser(user);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.valueOf(1000000000L + (long) (Math.random() * 9000000000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
