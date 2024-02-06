package pl.madej.finansemanangerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.madej.finansemanangerrestapi.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
