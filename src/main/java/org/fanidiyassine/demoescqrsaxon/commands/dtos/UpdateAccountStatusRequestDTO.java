package org.fanidiyassine.demoescqrsaxon.commands.dtos;

import org.fanidiyassine.demoescqrsaxon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus accountStatus) {
}
