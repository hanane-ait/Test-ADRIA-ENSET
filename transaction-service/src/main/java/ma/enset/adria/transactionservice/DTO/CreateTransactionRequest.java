package ma.enset.adria.transactionservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(

        @NotNull(message = "L'identifiant du compte est obligatoire")
        UUID accountId,

        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.00", inclusive = false,
                message = "Le montant doit être strictement positif")
        BigDecimal amount,

        @NotBlank(message = "La devise est obligatoire")
        @Pattern(regexp = "^[A-Za-z]{3}$",
                message = "La devise doit contenir 3 lettres")
        String currency,

        @NotBlank(message = "La catégorie du commerçant est obligatoire")
        String merchantCategory,

        @NotBlank(message = "Le pays est obligatoire")
        @Size(max = 100)
        String country

) {}