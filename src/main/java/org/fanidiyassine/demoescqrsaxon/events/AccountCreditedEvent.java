package org.fanidiyassine.demoescqrsaxon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountCreditedEvent {
    private String accountId;
    private double amount;
    private String currency;

}
