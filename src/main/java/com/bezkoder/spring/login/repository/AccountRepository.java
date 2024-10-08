package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Optional<Account> findByIdAndUserId(Long id, Long userId);
}
