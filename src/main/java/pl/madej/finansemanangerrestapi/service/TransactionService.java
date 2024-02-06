package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.mapper.TransactionMapper;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.payload.TransactionRequest;
import pl.madej.finansemanangerrestapi.repository.TransactionRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Long addTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionRequest);
        transaction.setDate(new Date());
        //transaction.setUser(user);

        return transactionRepository.save(transaction).getId();
    }
}
