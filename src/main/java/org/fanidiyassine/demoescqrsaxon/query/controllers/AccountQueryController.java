package org.fanidiyassine.demoescqrsaxon.query.controllers;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.fanidiyassine.demoescqrsaxon.query.dtos.AccountStatementResponseDTO;
import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.fanidiyassine.demoescqrsaxon.query.queries.GetAccountStatementQuery;
import org.fanidiyassine.demoescqrsaxon.query.queries.GetAllAccountsQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {
    private QueryGateway  queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts() {
        CompletableFuture<List<Account>> response = queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
        return response;
    }

    @GetMapping("/accountStatement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId){
        return queryGateway.query(new GetAccountStatementQuery(accountId), ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }
}
