package ma.enset.adria.transactionservice.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import ma.enset.adria.transactionservice.Client.AccountClient;
import ma.enset.adria.transactionservice.Client.AccountClientResponse;
import ma.enset.adria.transactionservice.DTO.CreateTransactionRequest;
import ma.enset.adria.transactionservice.DTO.TransferRequest;
import ma.enset.adria.transactionservice.DTO.TransactionResponse;
import ma.enset.adria.transactionservice.Client.AccountStatus;
import ma.enset.adria.transactionservice.Entities.Transaction;
import ma.enset.adria.transactionservice.Entities.TransactionType;
import ma.enset.adria.transactionservice.Exception.TransactionNotFoundException;
import ma.enset.adria.transactionservice.Repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    @Value("${bank.transaction.max-amount:1000}")
    private BigDecimal maxAmount;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountClient accountClient) {

        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
    }

    // Créer une transaction de débit
    @Override
    public TransactionResponse createTransaction(
            CreateTransactionRequest request) {

        validateAmount(request.amount());

        if (request.amount().compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException(
                    "Le montant dépasse la limite autorisée : " + maxAmount
            );
        }

        AccountClientResponse account =
                getAccount(request.accountId());

        validateAccount(account, request.currency());

        // Vérifier le solde
        if (BigDecimal.valueOf(account.balance())
                .compareTo(request.amount()) < 0) {

            throw new IllegalArgumentException("Solde insuffisant");
        }

        // Enregistrer la transaction
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                request.accountId(),
                null,
                request.amount(),
                request.currency().toUpperCase(),
                request.merchantCategory(),
                request.country(),
                TransactionType.DEBIT,
                Instant.now()
        );

        Transaction saved =
                transactionRepository.save(transaction);

        // Mettre à jour le solde du compte
        debitAccount(request.accountId(), request.amount());

        return toResponse(saved);
    }

    // Effectuer un transfert entre deux comptes
    @Override
    public TransactionResponse transfer(TransferRequest request) {

        validateAmount(request.amount());

        if (request.amount().compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException(
                    "Le montant dépasse la limite autorisée : " + maxAmount
            );
        }

        if (request.sourceAccountId()
                .equals(request.destinationAccountId())) {

            throw new IllegalArgumentException(
                    "Le compte source et le compte destinataire doivent être différents"
            );
        }

        AccountClientResponse source =
                getAccount(request.sourceAccountId());

        AccountClientResponse destination =
                getAccount(request.destinationAccountId());

        validateAccount(source, request.currency());
        validateAccount(destination, request.currency());

        if (BigDecimal.valueOf(source.balance())
                .compareTo(request.amount()) < 0) {

            throw new IllegalArgumentException("Solde insuffisant");
        }

        // Créer et enregistrer le transfert
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.amount(),
                request.currency().toUpperCase(),
                "TRANSFER",
                request.country(),
                TransactionType.TRANSFER,
                Instant.now()
        );

        Transaction saved =
                transactionRepository.save(transaction);

        // Débiter le compte source
        debitAccount(request.sourceAccountId(), request.amount());

        // Créditer le compte destinataire
        creditAccount(request.destinationAccountId(), request.amount());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(UUID id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction introuvable : " + id
                ));

        return toResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(
            UUID accountId) {

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Communication avec Account-Service

    @CircuitBreaker(
            name = "accountService",
            fallbackMethod = "accountFallback"
    )
    public AccountClientResponse getAccount(UUID id) {
        return accountClient.getAccount(id);
    }

    @CircuitBreaker(
            name = "accountService",
            fallbackMethod = "debitFallback"
    )
    public void debitAccount(UUID id, BigDecimal amount) {
        accountClient.debit(id, amount.doubleValue());
    }

    @CircuitBreaker(
            name = "accountService",
            fallbackMethod = "creditFallback"
    )
    public void creditAccount(UUID id, BigDecimal amount) {
        accountClient.credit(id, amount.doubleValue());
    }

    // Méthodes de secours Resilience4J

    private AccountClientResponse accountFallback(
            UUID id, Throwable exception) {

        throw new IllegalStateException(
                "Account-Service est temporairement indisponible",
                exception
        );
    }

    private void debitFallback(
            UUID id, BigDecimal amount, Throwable exception) {

        throw new IllegalStateException(
                "Impossible de débiter le compte : Account-Service indisponible",
                exception
        );
    }

    private void creditFallback(
            UUID id, BigDecimal amount, Throwable exception) {

        throw new IllegalStateException(
                "Impossible de créditer le compte : Account-Service indisponible",
                exception
        );
    }

    // Vérifier le compte et la devise

    private void validateAccount(
            AccountClientResponse account,
            String currency) {

        if (account.status() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Le compte est bloqué"
            );
        }

        if (!account.currency().equalsIgnoreCase(currency)) {
            throw new IllegalArgumentException(
                    "La devise de la transaction ne correspond pas à celle du compte"
            );
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant doit être strictement positif"
            );
        }
    }

    private TransactionResponse toResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getDestinationAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getMerchantCategory(),
                transaction.getCountry(),
                transaction.getType(),
                transaction.getCreatedAt()
        );
    }
}