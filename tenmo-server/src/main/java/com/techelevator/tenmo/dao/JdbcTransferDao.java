package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int createRequest(Transfer transfer) {
        int newId = 0;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?) RETURNING transfer_id";
        newId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
        if (transfer.getTransferTypeId() == 2) {
            transferMoney(transfer);
        }
        return newId;
    }

    @Override
    public boolean changeRequestStatus(Transfer transfer) {
        boolean success = false;
        try {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
            jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
            if (transfer.getTransferStatusId() == 2) {
                transferMoney(transfer);
            }
            success = true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    @Override
    public boolean transferMoney(Transfer transfer) {
        boolean success = false;
        try {
            String sql = "START TRANSACTION; UPDATE account SET balance = (balance - ?) WHERE account_id = ?; UPDATE account SET balance = (balance + ?) WHERE account_id = ?; COMMIT TRANSACTION;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFromId(), transfer.getAmount(), transfer.getAccountToId());
            success = true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }

    @Override
    public Transfer transferDetails(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer  WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            transfer = mapRowToTransfer(results);
        }return transfer;
    }
    @Override
    public String getFromUserName(int transferId){
        String name = null;
        String sql = "SELECT username FROM tenmo_user AS tu JOIN account AS a ON tu.user_id = a.user_id JOIN transfer AS t ON a.account_id = t.account_from WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            name = results.getString("username");
        }
        return name;
    }
    @Override
    public String getToUserName(int transferId){
        String name = null;
        String sql = "SELECT username FROM tenmo_user AS tu JOIN account AS a ON tu.user_id = a.user_id JOIN transfer AS t ON a.account_id = t.account_to WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            name = results.getString("username");
        }
        return name;
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
}
