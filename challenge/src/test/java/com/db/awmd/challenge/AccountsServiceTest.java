package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InSufficientBalanceOrAccountNotExistException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }


  @Test
  public void transferAccount() {
    Account acc1 = createAccount("id1", new BigDecimal(100));
    Account acc2 = createAccount("id2", new BigDecimal(200));
    this.accountsService.transferAmount(acc1, acc2, new BigDecimal(50));
    assertEquals(acc1.getBalance(), new BigDecimal(50));
    assertEquals(acc2.getBalance(), new BigDecimal(250));
  }

  @Test
  public void transferAccount_fail() {
    Account acc1 = createAccount("id3", new BigDecimal(10));
    Account acc2 = createAccount("id4", new BigDecimal(200));
    try {
      this.accountsService.transferAmount(acc1, acc2, new BigDecimal(50));
    } catch (InSufficientBalanceOrAccountNotExistException e) {
      assertEquals(e.getMessage(), "Balance is insufficient or Account doesn't Exist");
    }

  }


  private Account createAccount(String id, BigDecimal balance) {
    Account account = new Account(id);
    account.setBalance(balance);
    this.accountsService.createAccount(account);
    return account;
  }
}
