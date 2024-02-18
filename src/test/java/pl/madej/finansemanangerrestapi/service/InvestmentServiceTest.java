package pl.madej.finansemanangerrestapi.service;

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

    @BeforeEach
    public void init() {
        investment = new Investment();
        investment.setId(1L);
        investment.setType(InvestmentType.STOCK);
        investment.setQuantity(10);
        investment.setPurchasePrice(100.0);
        investment.setCurrentUserPrice(150.0);
        investment.setUser(new User());

        investmentRequest = new InvestmentRequest(
                1L,
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

    @Test
    public void updateInvestmentSuccessfully() {
        Investment updatedInvestment = new Investment();
        updatedInvestment.setId(1L);
        updatedInvestment.setType(InvestmentType.STOCK);
        updatedInvestment.setQuantity(50);
        updatedInvestment.setPurchasePrice(100.0);
        updatedInvestment.setCurrentUserPrice(170.0);
        updatedInvestment.setUser(new User());

        when(investmentRepository.findById(investment.getId())).thenReturn(Optional.of(investment));
        when(investmentRepository.save(investment)).thenReturn(updatedInvestment);

        InvestmentResponse investmentResponse = investmentService.updateInvestment(investmentRequest.getId(), investmentRequest);

        assertNotNull(investmentResponse);
        assertEquals(investmentResponse.getId(), updatedInvestment.getId());
        assertEquals(investmentResponse.getQuantity(), updatedInvestment.getQuantity());
        assertEquals(investmentResponse.getPurchasePrice(), updatedInvestment.getPurchasePrice());
        assertEquals(investmentResponse.getCurrentUserPrice(), updatedInvestment.getCurrentUserPrice());

        verify(investmentRepository, times(1)).findById(investment.getId());
        verify(investmentRepository, times(1)).save(investment);

    }
}
