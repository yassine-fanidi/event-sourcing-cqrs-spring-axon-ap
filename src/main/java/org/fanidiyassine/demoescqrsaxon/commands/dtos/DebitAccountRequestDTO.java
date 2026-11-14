package org.fanidiyassine.demoescqrsaxon.commands.dtos;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
