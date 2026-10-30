
package ma.enset.adria.accountservice1.AItools;

import ma.enset.adria.accountservice1.DTO.AccountResponse;
import ma.enset.adria.accountservice1.Entities.AccountStatus;
import ma.enset.adria.accountservice1.Service.AccountService;

import org.springframework.stereotype.Component;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;

import java.util.List;
import java.util.UUID;

@Component
public class AccountMcpTools {

    private final AccountService accountService;

    public AccountMcpTools(AccountService accountService) {
        this.accountService = accountService;
    }

    // 1. Consulter un compte
    @McpTool(description = "Consulter les informations d'un compte bancaire à partir de son identifiant.")
    public AccountResponse getAccount(
            @McpToolParam(description = "Identifiant UUID du compte") String id) {

        return accountService.getAccount(UUID.fromString(id));
    }

    // 2. Afficher tous les comptes
    @McpTool(description = "Afficher la liste de tous les comptes bancaires.")
    public List<AccountResponse> getAllAccounts() {

        return accountService.getAllAccounts();
    }

    // 3. Consulter le solde
    @McpTool(description = "Consulter le solde d'un compte bancaire.")
    public double getBalance(
            @McpToolParam(description = "Identifiant UUID du compte") String id) {

        return accountService.getBalance(UUID.fromString(id));
    }

    // 4. Bloquer ou activer un compte
    @McpTool(description = "Modifier le statut d'un compte bancaire : ACTIVE ou BLOCKED.")
    public AccountResponse changeStatus(
            @McpToolParam(description = "Identifiant UUID du compte") String id,
            @McpToolParam(description = "Nouveau statut : ACTIVE ou BLOCKED") String status) {

        AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());

        return accountService.changeStatus(
                UUID.fromString(id),
                accountStatus
        );
    }

    // 5. Débiter un compte
    @McpTool(description = "Débiter un montant du solde d'un compte bancaire.")
    public AccountResponse debit(
            @McpToolParam(description = "Identifiant UUID du compte") String id,
            @McpToolParam(description = "Montant à débiter") double amount) {

        return accountService.debit(UUID.fromString(id), amount);
    }

    // 6. Créditer un compte
    @McpTool(description = "Créditer un montant sur un compte bancaire.")
    public AccountResponse credit(
            @McpToolParam(description = "Identifiant UUID du compte") String id,
            @McpToolParam(description = "Montant à créditer") double amount) {

        return accountService.credit(UUID.fromString(id), amount);
    }
}
