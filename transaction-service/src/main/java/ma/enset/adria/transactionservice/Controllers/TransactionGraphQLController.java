package ma.enset.adria.transactionservice.Controllers;

import ma.enset.adria.transactionservice.DTO.CreateTransactionRequest;
import ma.enset.adria.transactionservice.DTO.TransferRequest;
import ma.enset.adria.transactionservice.DTO.TransactionResponse;
import ma.enset.adria.transactionservice.Service.TransactionService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public TransactionResponse transaction(@Argument String id) {
        return transactionService.getTransaction(UUID.fromString(id));
    }

    @QueryMapping
    public List<TransactionResponse> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public List<TransactionResponse> accountTransactions(
            @Argument String accountId) {

        return transactionService.getAccountTransactions(
                UUID.fromString(accountId)
        );
    }

    @MutationMapping
    public TransactionResponse createTransaction(
            @Argument CreateTransactionRequest input) {

        return transactionService.createTransaction(input);
    }

    @MutationMapping
    public TransactionResponse transfer(
            @Argument TransferRequest input) {

        return transactionService.transfer(input);
    }
}