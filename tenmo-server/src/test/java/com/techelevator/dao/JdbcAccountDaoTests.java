package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
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

public class JdbcAccountDaoTests extends BaseDaoTests {
    protected static final Account ACCOUNT_1 = new Account(1, 1001, new BigDecimal("1500.00"));
    protected static final Account ACCOUNT_2 = new Account(2, 1002, new BigDecimal("2000.00"));
    protected static final Account ACCOUNT_3 = new Account(3, 1003, new BigDecimal("2500.00"));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void viewCurrentBalance_should_return_balance(){
        BigDecimal actual = sut.viewCurrentBalance(2001);
        BigDecimal expected = new BigDecimal("1500.00");
        int bigDecimalEquals = 0;

        Assert.assertEquals(actual.compareTo(expected), 0);
    }

    @Test
    public void viewTransferHistory_should_return_all_transfers(){
        int expected = 2;
        List<Transfer> transferList = sut.viewTransferHistory(2001);
        int actual = transferList.size();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void viewPendingTransfers_should_return_all_pending_requests(){
        int expected = 1;
        List<Transfer> transferList = sut.viewPendingTransfers(2001);
        int actual = transferList.size();

        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void viewUsersToSendTo_should_return_all_other_usernames(){
//        int expected = 2;
//        List<String> usernames = sut.viewUsersToSendTo("user1");
//        int actual = usernames.size();
//
//        Assert.assertEquals(expected, actual);
//    }

}
