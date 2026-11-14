package org.fanidiyassine.demoescqrsaxon.command.dtos;

import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus accountStatus) {
}
