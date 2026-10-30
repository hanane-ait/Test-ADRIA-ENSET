package ma.enset.adria.transactionservice.Repository;

import ma.enset.adria.transactionservice.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByAccountId(UUID accountId);

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}