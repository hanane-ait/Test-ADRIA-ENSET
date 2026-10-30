package ma.enset.adria.transactionservice.Service;

import ma.enset.adria.transactionservice.DTO.CreateTransactionRequest;
import ma.enset.adria.transactionservice.DTO.TransferRequest;
import ma.enset.adria.transactionservice.DTO.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionResponse createTransaction(
            CreateTransactionRequest request
    );

    TransactionResponse transfer(TransferRequest request);

    TransactionResponse getTransaction(UUID id);

    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getAccountTransactions(UUID accountId);
}