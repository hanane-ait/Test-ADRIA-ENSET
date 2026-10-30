package ma.enset.adria.accountservice1.Controllers;

import ma.enset.adria.accountservice1.DTO.AccountResponse;
import ma.enset.adria.accountservice1.DTO.CreateAccountRequest;
import ma.enset.adria.accountservice1.Service.AccountService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class AccountGraphQLController {

    private final AccountService accountService;

    public AccountGraphQLController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Consulter un compte par son identifiant
    @QueryMapping
    public AccountResponse account(@Argument String id) {
        return accountService.getAccount(UUID.fromString(id));
    }

    // Consulter tous les comptes
    @QueryMapping
    public List<AccountResponse> accounts() {
        return accountService.getAllAccounts();
    }

    // Consulter le solde d'un compte
    @QueryMapping
    public double balance(@Argument String id) {
        return accountService.getBalance(UUID.fromString(id));
    }

    // Créer un compte
    @MutationMapping
    public AccountResponse createAccount(
            @Argument CreateAccountRequest input) {

        return accountService.createAccount(input);
    }
}