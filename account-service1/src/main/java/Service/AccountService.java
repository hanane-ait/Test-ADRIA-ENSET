package Service;

import ma.enset.adria.accountservice1.DTO.AccountResponse;
import ma.enset.adria.accountservice1.DTO.CreateAccountRequest;
import ma.enset.adria.accountservice1.Entities.AccountStatus;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse getAccount(UUID id);

    List<AccountResponse> getAllAccounts();

    double getBalance(UUID id);

    AccountResponse changeStatus(UUID id, AccountStatus status);

    AccountResponse debit(UUID id, double amount);

    AccountResponse credit(UUID id, double amount);
}