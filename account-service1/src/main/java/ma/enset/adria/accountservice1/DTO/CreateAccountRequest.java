package ma.enset.adria.accountservice1.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAccountRequest(

        @NotBlank(message = "La référence client est obligatoire")
        String customerReference,

        @NotBlank(message = "La devise est obligatoire")
        @Pattern(
                regexp = "^[A-Za-z]{3}$",
                message = "La devise doit contenir exactement 3 lettres"
        )
        String currency,

        @NotNull(message = "Le solde initial est obligatoire")
        @PositiveOrZero(message = "Le solde doit être positif ou nul")
        Double balance

) {
}