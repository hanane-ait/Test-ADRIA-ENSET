package ma.enset.adria.transactionservice.Controllers;

import jakarta.validation.Valid;

import ma.enset.adria.transactionservice.DTO.CreateTransactionRequest;
import ma.enset.adria.transactionservice.DTO.TransferRequest;
import ma.enset.adria.transactionservice.DTO.TransactionResponse;
import ma.enset.adria.transactionservice.Service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Créer une transaction
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(request));
    }

    // Effectuer un transfert
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.transfer(request));
    }

    // Consulter une transaction
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                transactionService.getTransaction(id)
        );
    }

    // Consulter toutes les transactions
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }

    // Consulter les transactions d'un compte
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @PathVariable UUID accountId) {

        return ResponseEntity.ok(
                transactionService.getAccountTransactions(accountId)
        );
    }
}