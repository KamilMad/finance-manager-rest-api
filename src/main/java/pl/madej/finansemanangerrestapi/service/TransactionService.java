package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.error.TransactionNotFoundException;
import pl.madej.finansemanangerrestapi.error.UnauthorizedAccessException;
import pl.madej.finansemanangerrestapi.mapper.TransactionMapper;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionRequest;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionResponse;
import pl.madej.finansemanangerrestapi.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public Long addTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionRequest);
        transaction.setDate(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);
        transaction.setUser(loggedUser);

        return transactionRepository.save(transaction).getId();
    }

    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id " + transactionId));

        //checks if user is owner of transaction
        validateTransactionOwnership(transaction);

        transactionRepository.deleteById(transactionId);
    }


    public TransactionResponse updateTransaction(Long transactionId, TransactionRequest transactionRequest) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id " + transactionId));

        //checks if user is owner of transaction
        validateTransactionOwnership(transaction);

        TransactionMapper.INSTANCE.updateTransactionFromDto(transactionRequest, transaction);

        Transaction savedTransaction  = transactionRepository.save(transaction);

        return TransactionMapper.INSTANCE.toTransactionResponse(savedTransaction);
    }

    public TransactionResponse getTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id " + transactionId));

        return TransactionMapper.INSTANCE.toTransactionResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper.INSTANCE::toTransactionResponse)
                .collect(Collectors.toList());
    }

    private void validateTransactionOwnership(Transaction transaction) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);

        if (!transaction.getUser().equals(loggedUser)) {
            throw new UnauthorizedAccessException("User does not have permission to delete this transaction.");
        }
    }

}
