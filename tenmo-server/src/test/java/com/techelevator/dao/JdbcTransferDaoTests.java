package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {
    Transfer transfer1 = new Transfer(1, 2, 2001, 2003, new BigDecimal("500.00"));
    Transfer transfer2 = new Transfer(2, 3, 2002, 2003, new BigDecimal("5.00"));
    Transfer transfer3 = new Transfer(1, 2, 2003, 2001, new BigDecimal("50.00"));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void createRequest_should_insert_transfer(){
        int expected = 3005;
        int actual = sut.createRequest(transfer1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void changeRequestStatus_changes_request_status(){
        transfer2.setTransferStatusId(2);
        boolean result = sut.changeRequestStatus(transfer2);
        Assert.assertTrue(result);
    }

    @Test
    public void transferMoney_transfers_money(){
        boolean result = sut.transferMoney(transfer1);
        Assert.assertTrue(result);
    }
    }