package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping("/account/")
@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {
    private AccountDao dao;
    private AccountService accountService;


    public AccountController(AccountDao dao, AccountService accountService){
        this.dao = dao;
        this.accountService = accountService;
    }

    @RequestMapping(path = "transfer-history", method = RequestMethod.GET)
    public List<Transfer> transferHistoryControl(Principal principal){
        return accountService.viewTransferHistoryService(principal.getName());
    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal viewBalanceControl(Principal principal){
        return accountService.viewBalanceService(principal.getName());
    }

    @RequestMapping(path = "pending-requests", method = RequestMethod.GET)
    public List<Transfer> pendingRequestsControl(Principal principal){
        return accountService.viewPendingTransferService(principal.getName());
    }

    @RequestMapping(path = "get-account-id", method = RequestMethod.GET)
    public int accountId(Principal principal){
        return dao.findAccountIdByUsername(principal.getName());
    }

    @RequestMapping(path = "request-users", method = RequestMethod.GET)
    public List<User> getUsersToTransferBetween(Principal principal){
        return dao.viewUsersToSendTo(principal.getName());
    }

    @RequestMapping(path = "request-username", method = RequestMethod.GET)
    public String getUsernameFromAccountId(@RequestBody int accountId){
        return dao.getUsernameByAccountId(accountId);
    }

    @RequestMapping(path = "request-accounts", method = RequestMethod.GET)
    public List<Account> getAccountsToTransferBetween(Principal principal){
        return dao.viewAccountsToTransferBetween(principal.getName());
    }
}
