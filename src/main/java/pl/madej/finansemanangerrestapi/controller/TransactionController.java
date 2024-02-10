package pl.madej.finansemanangerrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.madej.finansemanangerrestapi.payload.TransactionResponse;
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
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAllTransactions() {

        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok().body(transactions);
    }
}
