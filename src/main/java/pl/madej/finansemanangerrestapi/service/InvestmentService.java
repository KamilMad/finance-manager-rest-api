package pl.madej.finansemanangerrestapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.mapper.InvestmentMapper;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.repository.InvestmentRepository;

@Service
@AllArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;

    public Long addInvestment(InvestmentRequest investmentRequest) {

        Investment investment = InvestmentMapper.INSTANCE.toInvestment(investmentRequest);
        investment.setUser(new User());
        repository.save(investment);
    }
}
