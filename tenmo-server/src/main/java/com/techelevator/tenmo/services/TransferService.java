package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferService {
    JdbcTransferDao jdbcTransferDao;
    JdbcAccountDao jdbcAccountDao;

    TransferService(JdbcTransferDao jdbcTransferDao, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTransferDao = jdbcTransferDao;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    public int createRequestService(Transfer transfer, String username){
        Account fromAccount = jdbcAccountDao.getAccountById(transfer.getAccountFromId());
        if((transfer.getTransferTypeId() == 1 && transfer.getAccountToId() == jdbcAccountDao.findAccountIdByUsername(username)) || (transfer.getTransferTypeId() == 2 && transfer.getAccountFromId() == jdbcAccountDao.findAccountIdByUsername(username))) {
            if (transfer.getAccountFromId() != transfer.getAccountToId()) {
                if (transfer.getTransferTypeId() == 1 || (fromAccount.getBalance().compareTo(transfer.getAmount()) > -1)) {
                    return jdbcTransferDao.createRequest(transfer);
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    public boolean changeRequestStatusService(Transfer transfer, String username){
        if(transfer.getAccountFromId() == jdbcAccountDao.findAccountIdByUsername(username)) {
            Account fromAccount = jdbcAccountDao.getAccountById(transfer.getAccountFromId());
            if (transfer.getAccountFromId() != transfer.getAccountToId()) {
                return jdbcTransferDao.changeRequestStatus(transfer);
                }
                return false;
            }
            return false;
        }

    public String getOtherUsernameService(int transferId, String username){
        int selfAccountId = jdbcAccountDao.findAccountIdByUsername(username);
        String otherUser = null;
        Transfer transfer = jdbcTransferDao.transferDetails(transferId);
        if(selfAccountId == transfer.getAccountFromId()){
           otherUser = jdbcTransferDao.getToUserName(transferId);
        }else if(selfAccountId == transfer.getAccountToId()){
            otherUser = jdbcTransferDao.getFromUserName(transferId);
        }
        return  otherUser;
    }

    public String getMyUsernameService(int transferId, String username){
        int selfAccountId = jdbcAccountDao.findAccountIdByUsername(username);
        String myUsername = null;
        Transfer transfer = jdbcTransferDao.transferDetails(transferId);
        if(selfAccountId == transfer.getAccountFromId()){
            myUsername = jdbcTransferDao.getFromUserName(transferId);
        }else if(selfAccountId == transfer.getAccountToId()){
            myUsername = jdbcTransferDao.getToUserName(transferId);
        }
        return  myUsername;
    }
}
