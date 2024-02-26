package pl.madej.finansemanangerrestapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.error.InvestmentNotFoundException;
import pl.madej.finansemanangerrestapi.mapper.InvestmentMapper;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.repository.InvestmentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;

    public Long addInvestment(InvestmentRequest investmentRequest) {

        Investment investment = InvestmentMapper.INSTANCE.toInvestment(investmentRequest);
        investment.setUser(new User());

        return repository.save(investment).getId();
    }

    public void deleteInvestment(Long investmentId) {

        repository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with id " + investmentId.intValue()));

        repository.deleteById(investmentId);
    }

    public InvestmentResponse updateInvestment(Long investmentId, InvestmentRequest investmentRequest) {

        Investment investment = repository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with id " + investmentId.intValue()));

        InvestmentMapper.INSTANCE.updateTransactionFromDto(investmentRequest, investment);

        Investment savedInvestment = repository.save(investment);

        return InvestmentMapper.INSTANCE.toInvestmentResponse(savedInvestment);
    }

    public InvestmentResponse getInvestment(Long investmentId) {

        Investment investment = repository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with id " + investmentId.intValue()));

        return InvestmentMapper.INSTANCE.toInvestmentResponse(investment);
    }

    public List<InvestmentResponse> getAllInvestment() {

        return repository.findAll()
                .stream()
                .map(investment -> InvestmentMapper.INSTANCE.toInvestmentResponse(investment))
                .collect(Collectors.toList());
    }

}
