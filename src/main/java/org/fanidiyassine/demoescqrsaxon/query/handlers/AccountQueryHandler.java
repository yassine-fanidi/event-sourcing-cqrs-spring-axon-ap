package org.fanidiyassine.demoescqrsaxon.query.handlers;

import org.axonframework.queryhandling.QueryHandler;
import org.fanidiyassine.demoescqrsaxon.query.dtos.AccountStatementResponseDTO;
import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.fanidiyassine.demoescqrsaxon.query.entities.AccountOperation;
import org.fanidiyassine.demoescqrsaxon.query.queries.GetAccountStatementQuery;
import org.fanidiyassine.demoescqrsaxon.query.queries.GetAllAccountsQuery;
import org.fanidiyassine.demoescqrsaxon.query.repositories.AccountRepository;
import org.fanidiyassine.demoescqrsaxon.query.repositories.OperationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository  operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, operations);
    }
}
