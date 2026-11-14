package org.fanidiyassine.demoescqrsaxon.command.dtos;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}
