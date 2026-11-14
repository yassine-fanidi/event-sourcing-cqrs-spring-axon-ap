package org.fanidiyassine.demoescqrsaxon.command.dtos;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
