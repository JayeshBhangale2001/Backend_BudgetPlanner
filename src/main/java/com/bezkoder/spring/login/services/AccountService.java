package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.models.Account;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.AccountRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Account> getAllAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> getAccountByIdAndUserId(Long accountId, Long userId) {
        return accountRepository.findByIdAndUserId(accountId, userId);
    }

    public Account saveAccount(Long userId, Account account) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(account::setUser);
        return accountRepository.save(account);
    }

    public Account updateAccount(Long userId, Long accountId, Account accountDetails) {
        return accountRepository.findByIdAndUserId(accountId, userId)
                .map(account -> {
                    account.setAccountName(accountDetails.getAccountName());
                    account.setAccountDetails(accountDetails.getAccountDetails());
                    account.setAccountType(accountDetails.getAccountType());
                    account.setAccountAddress(accountDetails.getAccountAddress());
                    account.setInitialBalance(accountDetails.getInitialBalance());
                    account.setCurrentBalance(accountDetails.getCurrentBalance());
                    account.setNotes(accountDetails.getNotes());
                    return accountRepository.save(account);
                })
                .orElse(null);
    }

    public void deleteAccount(Long userId, Long accountId) {
        accountRepository.findByIdAndUserId(accountId, userId)
                .ifPresent(accountRepository::delete);
    }
}
