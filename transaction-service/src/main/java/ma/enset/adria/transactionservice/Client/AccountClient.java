package ma.enset.adria.transactionservice.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "account-service",
        url = "${account.service.url}"
)
public interface AccountClient {

    @GetMapping("/api/accounts/{id}")
    AccountClientResponse getAccount(@PathVariable UUID id);

    @PostMapping("/api/accounts/{id}/debit")
    AccountClientResponse debit(
            @PathVariable UUID id,
            @RequestParam double amount
    );

    @PostMapping("/api/accounts/{id}/credit")
    AccountClientResponse credit(
            @PathVariable UUID id,
            @RequestParam double amount
    );
}