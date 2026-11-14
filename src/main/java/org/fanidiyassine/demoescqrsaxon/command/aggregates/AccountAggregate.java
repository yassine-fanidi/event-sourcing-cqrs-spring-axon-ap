package org.fanidiyassine.demoescqrsaxon.command.aggregates;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.fanidiyassine.demoescqrsaxon.command.commands.AddAccountCommand;
import org.fanidiyassine.demoescqrsaxon.command.commands.CreditAccountCommand;
import org.fanidiyassine.demoescqrsaxon.command.commands.DebitAccountCommand;
import org.fanidiyassine.demoescqrsaxon.command.commands.UpdateAccountStatusCommand;
import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;
import org.fanidiyassine.demoescqrsaxon.events.*;

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
//        this.accountId = event.getAccountId();
        this.status = event.getAccountStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("################# Received CreditAccountCommand #################");
        //Fonction de décision
        //Régle Metier
        if(!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not activated.");
        if(command.getAmount()<=0){
            throw new IllegalArgumentException("Amount must be greater than 0.");
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
//        this.accountId = event.getAccountId();
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command){
        log.info("################# Received CreditAccountCommand #################");
        //Fonction de décision
        //Régle Metier
        if(!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not activated.");
        if(balance < command.getAmount()) throw new RuntimeException("Balance Not Enough.");
        if(command.getAmount()<=0){
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        log.info("################# AccountDebitedEvent occured #################");
//        this.accountId = event.getAccountId();
        this.balance -= event.getAmount();
    }
    @CommandHandler
    public void handle(UpdateAccountStatusCommand command){
        log.info("################# Received UpdateAccountStatusCommand #################");
        //Fonction de décision
        //Régle Metier
        if(command.getStatus() == status) throw new RuntimeException("The account "+command.getId()+" is already "+this.status);
        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(
                command.getId(),
                command.getStatus()
        ));
    }

    @EventSourcingHandler
    public void on(AccountStatusUpdatedEvent event){
        log.info("################# AccountStatusUpdatedEvent occured #################");
//        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }
}
