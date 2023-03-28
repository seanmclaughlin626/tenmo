package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountService {
    JdbcAccountDao jdbcAccountDao;

    AccountService(JdbcAccountDao jdbcAccountDao){
        this.jdbcAccountDao = jdbcAccountDao;
    }

    public List<Transfer> viewTransferHistoryService (String userName){
        int accountId = jdbcAccountDao.findAccountIdByUsername(userName);
        return jdbcAccountDao.viewTransferHistory(accountId);
    }
    public BigDecimal viewBalanceService (String userName){
        int accountId = jdbcAccountDao.findAccountIdByUsername(userName);
        return jdbcAccountDao.viewCurrentBalance(accountId);
    }

    public List<Transfer> viewPendingTransferService (String userName){
        int accountId = jdbcAccountDao.findAccountIdByUsername(userName);
        return jdbcAccountDao.viewPendingTransfers(accountId);
    }

}
