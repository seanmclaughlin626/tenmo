package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private  String authToken = null;

    public AccountService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal viewBalance(){
       BigDecimal result = null;
       try{
           ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
           result = response.getBody();
       }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public Transfer[] viewTransferHistory() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "account/transfer-history", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }return transfers;
    }

    public Transfer[] viewPendingRequests() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "account/pending-requests", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }return transfers;
    }

    public int getAccountId(){
        int result = 0;
        try{
            ResponseEntity<Integer> response = restTemplate.exchange(baseUrl + "account/get-account-id", HttpMethod.GET, makeAuthEntity(), Integer.class);
            result = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public User[] getOtherUsers(){
        User[] result = null;
        try{
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "account/request-users", HttpMethod.GET, makeAuthEntity(), User[].class);
            result = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public Account[] getOtherAccounts(){
        Account[] result = null;
        try{
            ResponseEntity<Account[]> response = restTemplate.exchange(baseUrl + "account/request-accounts", HttpMethod.GET, makeAuthEntity(), Account[].class);
            result = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
