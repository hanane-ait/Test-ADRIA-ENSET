package ma.enset.adria.accountservice1.Service;

import ma.enset.adria.accountservice1.DTO.AccountResponse;
import ma.enset.adria.accountservice1.DTO.CreateAccountRequest;
import ma.enset.adria.accountservice1.Entities.Account;
import ma.enset.adria.accountservice1.Entities.AccountStatus;
import ma.enset.adria.accountservice1.Exception.AccountBlockedException;
import ma.enset.adria.accountservice1.Exception.AccountNotFoundException;
import ma.enset.adria.accountservice1.Repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Créer un compte
    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {

        Account account = new Account(
                UUID.randomUUID(),
                request.customerReference(),
                AccountStatus.ACTIVE,
                request.currency().toUpperCase(),
                request.balance()
        );

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    // Consulter un compte
    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID id) {

        Account account = findAccount(id);

        return toResponse(account);
    }

    // Récupérer tous les comptes
    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Consulter le solde
    @Override
    @Transactional(readOnly = true)
    public double getBalance(UUID id) {

        Account account = findAccount(id);

        return account.getBalance();
    }

    // Modifier le statut du compte
    @Override
    public AccountResponse changeStatus(UUID id, AccountStatus status) {

        Account account = findAccount(id);

        account.setStatus(status);

        Account updatedAccount = accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    // Débiter un compte
    @Override
    public AccountResponse debit(UUID id, double amount) {

        validateAmount(amount);

        Account account = findAccount(id);

        checkAccountIsActive(account);

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Solde insuffisant");
        }

        account.setBalance(account.getBalance() - amount);

        return toResponse(accountRepository.save(account));
    }

    // Créditer un compte
    @Override
    public AccountResponse credit(UUID id, double amount) {

        validateAmount(amount);

        Account account = findAccount(id);

        checkAccountIsActive(account);

        account.setBalance(account.getBalance() + amount);

        return toResponse(accountRepository.save(account));
    }

    // Rechercher un compte ou lancer une exception
    private Account findAccount(UUID id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Compte introuvable : " + id
                        )
                );
    }

    // Vérifier que le compte est actif
    private void checkAccountIsActive(Account account) {

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(
                    "Le compte est bloqué"
            );
        }
    }

    // Vérifier le montant
    private void validateAmount(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Le montant doit être strictement positif"
            );
        }
    }

    // Convertir Entity vers DTO
    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getCustomerReference(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance()
        );
    }
}