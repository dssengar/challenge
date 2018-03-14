package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

  @Override
  public boolean transferAmount(Account transferFrom, Account transferTo, BigDecimal amount) {
    Lock lock = new ReentrantLock();
    try {
      lock.lock();
      if (transferFrom == null || transferTo == null || amount.compareTo(BigDecimal.ZERO) < 0 || transferFrom.getBalance().compareTo(amount) < 0) {
        return false;
      }
      transferFrom.setBalance(transferFrom.getBalance().subtract(amount));
      transferTo.setBalance(transferTo.getBalance().add(amount));
    } finally {
      lock.unlock();
    }
    return true;
  }


}
