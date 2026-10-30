package ma.enset.adria.transactionservice.Client;

import java.util.UUID;

public record AccountClientResponse(
        UUID id,
        String customerReference,
        AccountStatus status,
        String currency,
        double balance
) {
}