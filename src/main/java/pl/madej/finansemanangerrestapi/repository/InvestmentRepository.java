package pl.madej.finansemanangerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.madej.finansemanangerrestapi.model.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
