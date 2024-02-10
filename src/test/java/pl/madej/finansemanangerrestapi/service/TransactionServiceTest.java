package pl.madej.finansemanangerrestapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.madej.finansemanangerrestapi.model.Category;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.model.TransactionType;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.TransactionRequest;
import pl.madej.finansemanangerrestapi.payload.TransactionResponse;
import pl.madej.finansemanangerrestapi.repository.TransactionRepository;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Transaction transaction2;
    private TransactionRequest transactionRequest;
    @BeforeEach
    public void init() {
        transaction = new Transaction(1L, "Description1", 100.0, TransactionType.EXPENSE, Category.Groceries, LocalDateTime.now(), new User());
        transaction2 = new Transaction(2L, "Description2", 200.0, TransactionType.INCOME, Category.Groceries, LocalDateTime.now(), new User());
        transactionRequest = new TransactionRequest(1L, "Description", 100.0, TransactionType.EXPENSE, Category.Groceries);
    }

    @Test
    public void addTransactionSuccessfully() {
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(1L); // Simulate setting ID upon saving
            return t;
        });

        Long savedTransactionId = transactionService.addTransaction(transactionRequest);

        assertNotNull(savedTransactionId);
        assertEquals(1L, savedTransactionId.longValue());

        verify(transactionRepository).save(argThat(trans ->
                "Description".equals(trans.getDescription()) &&
                        100.0 == trans.getAmount() &&
                        TransactionType.EXPENSE == trans.getTransactionType() &&
                        Category.Groceries == trans.getCategory() && // Now directly comparing enum value
                        trans.getDate() != null &&
                        trans.getUser() != null
        ));
    }

    @Test
    public void deleteTransactionSuccessfully() {
        long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    public void testDeleteTransactionTransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.deleteTransaction(transactionId));
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    public void getTransactionSuccessfully() {
        long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionResponse obtainedTransactionResponse = transactionService.getTransaction(transactionId);

        verify(transactionRepository, times(1)).findById(transactionId);
        assertEquals(transaction.getId(),obtainedTransactionResponse.id());
        assertNotNull(obtainedTransactionResponse);
        assertEquals(transaction.getId(), obtainedTransactionResponse.id());
        assertEquals(transaction.getDescription(), obtainedTransactionResponse.description());
        assertEquals(transaction.getAmount(), obtainedTransactionResponse.amount());
        assertEquals(transaction.getCategory(), obtainedTransactionResponse.category());
        assertEquals(transaction.getTransactionType(), obtainedTransactionResponse.transactionType());
    }

    @Test
    void getTransactionTransactionNotFound() {
        long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,() -> transactionService.getTransaction(transactionId));
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void getAllTransactionsSuccessfully() {
        List<Transaction> transactions = List.of(transaction, transaction2);

        List<TransactionResponse> expectedResponses = transactions.stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getCategory()
                )).toList();

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionResponse> actualResponses = transactionService.getAllTransactions();

        assertEquals(expectedResponses.size(), actualResponses.size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            TransactionResponse expected = expectedResponses.get(i);
            TransactionResponse actual = actualResponses.get(i);
            assertEquals(expected.id(), actual.id());
            assertEquals(expected.description(), actual.description());
            assertEquals(expected.amount(), actual.amount());
            assertEquals(expected.transactionType(), actual.transactionType());
        }

        verify(transactionRepository).findAll();
    }

    @Test
    public void getAllTransactions_ReturnsEmptyListWhenNoTransactionsExist() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<TransactionResponse> actualResponses = transactionService.getAllTransactions();

        assertTrue(actualResponses.isEmpty(), "Expected an empty list of transaction responses");

        verify(transactionRepository).findAll();
    }

    @Test
    public void updateTransactionSuccessfully() {

        Transaction updatedTransaction = new Transaction(1L, "Updated description", 200.0,
                TransactionType.INCOME, Category.Groceries, LocalDateTime.now(), new User());

        when(transactionRepository.findById(transactionRequest.id())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);

        TransactionResponse transactionResponse = transactionService.updateTransaction(transactionRequest.id(), transactionRequest);

        assertNotNull(transactionResponse);
        assertEquals(transactionResponse.id(), 1L);
        assertEquals(transactionResponse.description(), "Updated description");
        assertEquals(transactionResponse.transactionType(), TransactionType.INCOME);
        assertEquals(transactionResponse.amount(), 200.0);

        verify(transactionRepository).findById(transactionRequest.id());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void updateTransactionTransactionNotFound() {

        when(transactionRepository.findById(transactionRequest.id())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,() -> transactionService.updateTransaction(transactionRequest.id(),transactionRequest));
        verify(transactionRepository, times(1)).findById(transactionRequest.id());
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
}
