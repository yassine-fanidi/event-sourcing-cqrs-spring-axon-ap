package org.fanidiyassine.demoescqrsaxon.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.fanidiyassine.demoescqrsaxon.commands.commands.AddAccountCommand;
import org.fanidiyassine.demoescqrsaxon.commands.commands.CreditAccountCommand;
import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;
import org.fanidiyassine.demoescqrsaxon.events.AccountActivitedEvent;
import org.fanidiyassine.demoescqrsaxon.events.AccountCreatedEvent;
import org.fanidiyassine.demoescqrsaxon.events.AccountCreditedEvent;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;
    //Pour Axon
    public AccountAggregate(){}

    @CommandHandler
    public AccountAggregate(AddAccountCommand command){
        log.info("################# Received AddAccountCommand #################");
        //Fonction de décision
        //Régle Metier
        if(command.getInitialBalance()<=0){
            throw new IllegalArgumentException("Initial Balance must be greater than 0.");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                AccountStatus.CREATED,
                command.getCurrency()
        ));
        AggregateLifecycle.apply(new AccountActivitedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        log.info("################# AccountCreatedEvent occured #################");
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.status = event.getAccountStatus();
    }
    @EventSourcingHandler
    public void on(AccountActivitedEvent event){
        log.info("################# AccountActivitedEvent occured #################");
        this.accountId = event.getAccountId();
        this.status = event.getAccountStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("################# Received CreditAccountCommand #################");
        //Fonction de décision
        //Régle Metier
        if(!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+"is not activated.");
        if(command.getAmount()<=0){
            throw new IllegalArgumentException("Initial Balance must be greater than 0.");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        log.info("################# AccountCreditedEvent occured #################");
        this.accountId = event.getAccountId();
        this.balance += event.getAmount();
    }
}
