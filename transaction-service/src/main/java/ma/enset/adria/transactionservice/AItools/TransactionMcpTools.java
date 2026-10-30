package ma.enset.adria.transactionservice.AItools;

import ma.enset.adria.transactionservice.DTO.CreateTransactionRequest;
import ma.enset.adria.transactionservice.DTO.TransactionResponse;
import ma.enset.adria.transactionservice.Service.TransactionService;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionMcpTools {

    private final TransactionService transactionService;

    public TransactionMcpTools(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @McpTool(description = "Consulter toutes les transactions bancaires")
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @McpTool(description = "Consulter une transaction par son identifiant")
    public TransactionResponse getTransaction(
            @McpToolParam(description = "Identifiant UUID de la transaction")
            String id) {

        return transactionService.getTransaction(UUID.fromString(id));
    }

    @McpTool(description = "Consulter les transactions d'un compte bancaire")
    public List<TransactionResponse> getAccountTransactions(
            @McpToolParam(description = "Identifiant UUID du compte")
            String accountId) {

        return transactionService.getAccountTransactions(
                UUID.fromString(accountId)
        );
    }

    @McpTool(description = "Créer une transaction bancaire")
    public TransactionResponse createTransaction(
            @McpToolParam(description = "Identifiant UUID du compte")
            String accountId,

            @McpToolParam(description = "Montant de la transaction")
            double amount,

            @McpToolParam(description = "Devise, par exemple MAD")
            String currency,

            @McpToolParam(description = "Catégorie du commerçant")
            String merchantCategory,

            @McpToolParam(description = "Pays de la transaction")
            String country) {

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        UUID.fromString(accountId),
                        BigDecimal.valueOf(amount),
                        currency,
                        merchantCategory,
                        country
                );

        return transactionService.createTransaction(request);
    }
}