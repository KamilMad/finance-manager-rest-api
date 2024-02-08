package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.mapper.TransactionMapper;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.TransactionRequest;
import pl.madej.finansemanangerrestapi.payload.TransactionResponse;
import pl.madej.finansemanangerrestapi.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Long addTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionRequest);
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(new User());

        return transactionRepository.save(transaction).getId();
    }

    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + transactionId));
        transactionRepository.delete(transaction);
    }

    public TransactionResponse updateTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = transactionRepository.findById(transactionRequest.id())
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + transactionRequest.id()));

        TransactionMapper.INSTANCE.updateTransactionFromDto(transactionRequest, transaction);

        Transaction savedTransaction  = transactionRepository.save(transaction);

        return TransactionMapper.INSTANCE.toTransactionResponse(savedTransaction);
    }

    public TransactionResponse getTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + transactionId));

        return TransactionMapper.INSTANCE.toTransactionResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> TransactionMapper.INSTANCE.toTransactionResponse(transaction))
                .collect(Collectors.toList());
    }

}
