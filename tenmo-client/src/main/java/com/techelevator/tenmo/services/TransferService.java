package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private  String authToken = null;

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int createRequest(Transfer newTransfer){
        int returnedTransferId = 0;
        try{
            ResponseEntity<Integer> response = restTemplate.exchange(baseUrl + "transfer/", HttpMethod.POST, makeTransferEntity(newTransfer), Integer.class);
            returnedTransferId = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            BasicLogger.log(e.getMessage());
        }return  returnedTransferId;
    }

    public boolean changeRequestStatus(Transfer updatedTransfer){
        boolean success = false;
        try{
            restTemplate.put(baseUrl + "transfer/update-request-status", makeTransferEntity(updatedTransfer));
            success = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/transfer-details/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }return transfer;
    }

    public String getOtherUserName(int transferId){
        String username = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "transfer/other-username/" + transferId, HttpMethod.GET, makeAuthEntity(), String.class);
            username = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    public String getFromUserName(int transferId){
        String username = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "transfer/from-username/" + transferId, HttpMethod.GET, makeAuthEntity(), String.class);
            username = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    public String getToUserName(int transferId){
        String username = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "transfer/to-username/" + transferId, HttpMethod.GET, makeAuthEntity(), String.class);
            username = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
