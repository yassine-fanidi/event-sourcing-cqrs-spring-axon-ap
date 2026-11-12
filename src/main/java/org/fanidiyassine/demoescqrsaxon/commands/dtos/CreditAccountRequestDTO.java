package org.fanidiyassine.demoescqrsaxon.commands.dtos;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}
