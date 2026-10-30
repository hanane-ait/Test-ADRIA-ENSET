package ma.enset.adria.accountservice1.DTO;

import ma.enset.adria.accountservice1.Entities.AccountStatus;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String customerReference,
        AccountStatus status,
        String currency,
        double balance
) {
}