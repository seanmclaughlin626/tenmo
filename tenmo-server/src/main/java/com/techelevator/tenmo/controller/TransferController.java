package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/transfer/")
@RestController
@PreAuthorize("isAuthenticated()")

public class TransferController {
    JdbcTransferDao jdbcTransferDao;
    TransferService transferService;

   public  TransferController(JdbcTransferDao jdbcTransferDao,  TransferService transferService){
        this.jdbcTransferDao = jdbcTransferDao;
        this.transferService = transferService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public int createRequestControl(@Valid @RequestBody Transfer transfer, Principal principal){
       return transferService.createRequestService(transfer, principal.getName());
    }

    @RequestMapping(path = "update-request-status", method = RequestMethod.PUT)
    public boolean changeRequestStatusControl(@Valid @RequestBody Transfer transfer, Principal principal){
       return transferService.changeRequestStatusService(transfer, principal.getName());
    }

    @RequestMapping(path = "transfer-details/{id}", method = RequestMethod.GET)
    public Transfer transferDetails(@PathVariable int id){
       return jdbcTransferDao.transferDetails(id);

    }

    @RequestMapping(path = "other-username/{id}", method = RequestMethod.GET)
    public String otherUserNameControl(@PathVariable int id, Principal principal){
       return transferService.getOtherUsernameService(id, principal.getName());
    }

    @RequestMapping(path = "from-username/{id}", method = RequestMethod.GET)
    public String getFromUserNameControl(@PathVariable int id){
        return jdbcTransferDao.getFromUserName(id);
    }

    @RequestMapping(path = "to-username/{id}", method = RequestMethod.GET)
    public String getToUserNameControl(@PathVariable int id){
        return jdbcTransferDao.getToUserName(id);
    }
}
