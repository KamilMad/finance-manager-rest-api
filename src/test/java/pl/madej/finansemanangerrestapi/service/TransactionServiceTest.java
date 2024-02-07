package pl.madej.finansemanangerrestapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.madej.finansemanangerrestapi.mapper.TransactionMapper;
import pl.madej.finansemanangerrestapi.model.Category;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.model.TransactionType;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.TransactionRequest;
import pl.madej.finansemanangerrestapi.repository.TransactionRepository;


import java.time.LocalDateTime;
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

    @BeforeEach
    public void init() {

    }

    @Test
    public void addTransactionSuccessfully() {
        // Given
        TransactionRequest transactionRequest = new TransactionRequest(
                1L, "desc", 100.0, TransactionType.EXPENSE, Category.Groceries
        );
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(1L); // Simulate setting ID upon saving
            return t;
        });

        // When
        Long savedTransactionId = transactionService.addTransaction(transactionRequest);

        // Then
        assertNotNull(savedTransactionId);
        assertEquals(1L, savedTransactionId.longValue());

        // Verify that save method was called with a Transaction having expected properties
        verify(transactionRepository).save(argThat(trans ->
                "desc".equals(trans.getDescription()) &&
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

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Grocery shopping");
        transaction.setAmount(50.0);
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setCategory(Category.Groceries);
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(new User());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    public void testDeleteTransactionTransactionNotFound() {
        // Given
        Long transactionId = 1L;

        // Mock the behavior of the repository
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(RuntimeException.class, () -> transactionService.deleteTransaction(transactionId));
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).delete(any());
    }


}
