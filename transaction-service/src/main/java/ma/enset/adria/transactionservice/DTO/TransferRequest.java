package ma.enset.adria.transactionservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(

        @NotNull(message = "Le compte source est obligatoire")
        UUID sourceAccountId,

        @NotNull(message = "Le compte destinataire est obligatoire")
        UUID destinationAccountId,

        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.00", inclusive = false)
        BigDecimal amount,

        @NotBlank(message = "La devise est obligatoire")
        @Pattern(regexp = "^[A-Za-z]{3}$")
        String currency,

        @NotBlank(message = "Le pays est obligatoire")
        String country

) {}