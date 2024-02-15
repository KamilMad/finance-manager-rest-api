package pl.madej.finansemanangerrestapi.service;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.madej.finansemanangerrestapi.error.InvestmentNotFoundException;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.repository.InvestmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvestmentServiceTest {

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
        investment.setType(InvestmentType.STOCK);
        investment.setQuantity(10);
        investment.setPurchasePrice(100.0);
        investment.setCurrentUserPrice(150.0);
        investment.setUser(new User());

        investmentRequest = new InvestmentRequest(
                InvestmentType.STOCK,
                10,
                100.0,
                150.0
        );

        investmentResponse = new InvestmentResponse(
                InvestmentType.STOCK,
                10,
                100.0,
                150.0
        );
    }


    @Test
    public void testAddInvestmentSuccessful() {

        when(investmentRepository.save(any(Investment.class))).thenAnswer(i -> {
            Investment inv = i.getArgument(0);
            inv.setId(1L);
            return inv;
        });

        Long savedInvestmentId = investmentService.addInvestment(investmentRequest);

        assertNotNull(savedInvestmentId);
        assertEquals(1L, savedInvestmentId);

        verify(investmentRepository).save(argThat(inv ->
               investment.getType().equals(inv.getType())
                        && investment.getQuantity() == inv.getQuantity()
                        && investment.getPurchasePrice() == inv.getPurchasePrice()
                        && investment.getCurrentUserPrice() == inv.getCurrentUserPrice()));

    }

    @Test
    public void deleteInvestmentSuccessfully() {
        Long investmentId = 1L;

        when(investmentRepository.findById(investmentId)).thenReturn(Optional.of(investment));

        investmentService.deleteInvestment(investmentId);

        verify(investmentRepository,times(1)).findById(investmentId);
        verify(investmentRepository,times(1)).deleteById(investmentId);
    }

    @Test
    public void deleteInvestmentInvestmentNotFound() {
        Long investmentId = 1L;

        when(investmentRepository.findById(investmentId)).thenReturn(Optional.empty());

        assertThrows(InvestmentNotFoundException.class, () -> investmentService.deleteInvestment(investmentId));
        verify(investmentRepository, times(1)).findById(investmentId);
        verify(investmentRepository, times(0)).deleteById(investmentId);

    }
}
