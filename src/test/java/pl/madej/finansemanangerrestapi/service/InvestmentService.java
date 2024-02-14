package pl.madej.finansemanangerrestapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.repository.InvestmentRepository;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvestmentService {

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentService investmentService;

    private Investment investment;
    private InvestmentRequest investmentRequest;
    private InvestmentResponse investmentResponse;

    @BeforeEach
    public void init() {
        investment = new Investment();
    }


    @Test
    public void testAddInvestmentSuccessful() {


        when(investmentRepository.save()).the
    }
}
