// AccountRestController.java
package ma.enset.adria.accountservice1.Controllers;
import jakarta.validation.Valid;
import ma.enset.adria.accountservice1.DTO.AccountResponse;
import ma.enset.adria.accountservice1.DTO.CreateAccountRequest;
import ma.enset.adria.accountservice1.Entities.AccountStatus;
import ma.enset.adria.accountservice1.Service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 1. Créer un compte
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response =
                accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Consulter un compte
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable UUID id) {

        return ResponseEntity.ok(accountService.getAccount(id));
    }

    // 3. Récupérer tous les comptes
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // 4. Consulter le solde
    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(
            @PathVariable UUID id) {

        return ResponseEntity.ok(accountService.getBalance(id));
    }

    // 5. Modifier le statut
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> changeStatus(
            @PathVariable UUID id,
            @RequestParam AccountStatus status) {

        return ResponseEntity.ok(
                accountService.changeStatus(id, status)
        );
    }

    // 6. Débiter un compte
    @PostMapping("/{id}/debit")
    public ResponseEntity<AccountResponse> debit(
            @PathVariable UUID id,
            @RequestParam double amount) {

        return ResponseEntity.ok(
                accountService.debit(id, amount)
        );
    }

    // 7. Créditer un compte
    @PostMapping("/{id}/credit")
    public ResponseEntity<AccountResponse> credit(
            @PathVariable UUID id,
            @RequestParam double amount) {

        return ResponseEntity.ok(
                accountService.credit(id, amount)
        );
    }
}