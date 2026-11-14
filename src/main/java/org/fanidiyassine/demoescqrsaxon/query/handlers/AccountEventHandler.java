package org.fanidiyassine.demoescqrsaxon.query.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.fanidiyassine.demoescqrsaxon.enums.OperationType;
import org.fanidiyassine.demoescqrsaxon.events.*;
import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.fanidiyassine.demoescqrsaxon.query.entities.AccountOperation;
import org.fanidiyassine.demoescqrsaxon.query.repositories.AccountRepository;
import org.fanidiyassine.demoescqrsaxon.query.repositories.OperationRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @EventHandler //Subscribe chez l'event Bus
    public void on(AccountCreatedEvent event, EventMessage eventMessage) {
        log.info("======================= Query side AccountCreatedEvent Received =======================");
        Account account = Account.builder()
                .id(event.getAccountId())
                .balance(event.getInitialBalance())
                .currency(event.getCurrency())
                .status(event.getAccountStatus())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
    }
    @EventHandler //Subscribe chez l'event Bus
    public void on(AccountActivitedEvent event) {
        log.info("======================= Query side AccountActivitedEvent Received =======================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getAccountStatus());
        accountRepository.save(account);
    }
    @EventHandler //Subscribe chez l'event Bus
    public void on(AccountCreditedEvent event, EventMessage eventMessage) {
        log.info("======================= Query side AccountCreditedEvent Received =======================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .operationType(OperationType.CREDIT)
                .account(account)
                .build();
//        account.setBalance(account.getBalance() + event.getAmount());
        account.setBalance(account.getBalance() - accountOperation.getAmount());
        operationRepository.save(accountOperation);
        accountRepository.save(account);
    }
    @EventHandler //Subscribe chez l'event Bus
    public void on(AccountDebitedEvent event, EventMessage eventMessage) {
        log.info("======================= Query side AccountDebitedEvent Received =======================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .operationType(OperationType.DEBIT)
                .account(account)
                .build();
        account.setBalance(account.getBalance() - event.getAmount());
//        account.setBalance(account.getBalance() - accountOperation.getAmount());
        operationRepository.save(accountOperation);
        accountRepository.save(account);
    }
    @EventHandler //Subscribe chez l'event Bus
    public void on(AccountStatusUpdatedEvent event) {
        log.info("======================= Query side AccountStatusUpdatedEvent Received =======================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }
}
