package ma.enset.adria.transactionservice.DTO;

import ma.enset.adria.transactionservice.Entities.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID destinationAccountId,
        BigDecimal amount,
        String currency,
        String merchantCategory,
        String country,
        TransactionType type,
        Instant createdAt
) {}