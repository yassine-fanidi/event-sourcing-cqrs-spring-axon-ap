package org.fanidiyassine.demoescqrsaxon.query.dtos;

import org.fanidiyassine.demoescqrsaxon.query.entities.Account;
import org.fanidiyassine.demoescqrsaxon.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account,
        List<AccountOperation> accountOperations
) {
}
