package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountDao {

    BigDecimal viewCurrentBalance(int acountId);

    List<Transfer> viewTransferHistory(int accountId);

    List<Transfer> viewPendingTransfers(int accountId);

    public int findAccountIdByUsername(String username);

    public Account getAccountById(int id);

    public List<User> viewUsersToSendTo (String userName);

    public String getUsernameByAccountId (int accountId);

    public List<Account> viewAccountsToTransferBetween(String userName);
}
