package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InSufficientBalanceOrAccountNotExistException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void transferAmount(Account transferFrom, Account transferTo, BigDecimal amount) {
    if (this.accountsRepository.transferAmount(transferFrom, transferTo, amount)) {
      notificationService.notifyAboutTransfer(transferFrom, amount + " Credited to Account:" + transferTo.getAccountId());
      notificationService.notifyAboutTransfer(transferTo, amount + " Credited from Account:" + transferFrom.getAccountId());
    } else {
      throw new InSufficientBalanceOrAccountNotExistException("Balance is insufficient or Account doesn't Exist");//Can be handle separately insufficient balance and account not exist
    }
  }
}
