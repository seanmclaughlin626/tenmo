package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal viewCurrentBalance(int accountId){
        BigDecimal resultDecimal = new BigDecimal("0.00");
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if(result.next()){
            resultDecimal = result.getBigDecimal("balance");
        }
        return resultDecimal;
    }

    @Override
    public List<Transfer> viewTransferHistory(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 2";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }

    @Override
    public List<Transfer> viewPendingTransfers(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from,account_to, amount FROM transfer WHERE account_from = ? AND transfer_status_id = 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }
    @Override
    public int findAccountIdByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        int accountId;
        try {
            accountId = jdbcTemplate.queryForObject("SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?", int.class, username);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }

        return accountId;
    }

    @Override
    public List<User> viewUsersToSendTo (String userName){
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT user_id, username FROM tenmo_user WHERE username != ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userName);
        while(results.next()){
            usersList.add(mapRowToUser(results));
        }
        return usersList;
    }

    @Override
    public String getUsernameByAccountId(int id){
        String username = null;
        String sql = "SELECT username FROM tenmo_user AS u JOIN account AS a ON u.user_id = a.user_id WHERE a.account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next()){
            username = results.getString("username");
        }
        return username;
    }

    public List<Account> viewAccountsToTransferBetween(String username){
        List<Account> accountsList = new ArrayList<>();
        String sql = "SELECT account_id, u.user_id, balance FROM account AS a JOIN tenmo_user AS u ON a.user_id = u.user_id WHERE u.username != ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while(results.next()){
            accountsList.add(mapRowToAccount(results));
        }
        return accountsList;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));

        return transfer;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));

        return account;
    }

    private User mapRowToUser(SqlRowSet rowSet){
        User user = new User();
        user.setId(rowSet.getInt("user_id"));
        user.setUsername(rowSet.getString("username"));

        return user;
    }
}