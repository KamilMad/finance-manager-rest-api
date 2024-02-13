package pl.madej.finansemanangerrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionRequest;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionResponse;
import pl.madej.finansemanangerrestapi.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> findTransaction(@PathVariable Long transactionId) {

        TransactionResponse response = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAllTransactions() {

        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Long> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        Long id = transactionService.addTransaction(transactionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long transactionId, @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.updateTransaction(transactionId, transactionRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);

        return ResponseEntity.noContent().build();
    }
}
