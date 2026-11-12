package org.fanidiyassine.demoescqrsaxon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountActivitedEvent {
    private String accountId;
    private AccountStatus accountStatus;

}
