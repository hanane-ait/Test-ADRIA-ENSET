package ma.enset.adria.accountservice1.Repository;

import ma.enset.adria.accountservice1.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<Account, UUID> {

    boolean existsByCustomerReference(String customerReference);
}