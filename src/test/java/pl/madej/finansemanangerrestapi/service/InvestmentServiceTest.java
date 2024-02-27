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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private Investment investment2;
    private InvestmentRequest investmentRequest;

    @BeforeEach
    public void init() {
        investment = new Investment(1L, InvestmentType.STOCK, 10, 100.0, 150.0, new User());
        investment2 = new Investment(2L, InvestmentType.STOCK, 30, 80.0, 50.0, new User());

        investmentRequest = new InvestmentRequest(
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

    @Test
    public void updateInvestmentNotFound() {
        when(investmentRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvestmentNotFoundException.class,() -> investmentService.updateInvestment(investmentRequest.getId(), investmentRequest));
        verify(investmentRepository, times(1)).findById(investmentRequest.getId());
        verify(investmentRepository, times(0)).save(any(Investment.class));
    }

    @Test
    public void getInvestmentSuccessfully() {

        when(investmentRepository.findById(investment.getId())).thenReturn(Optional.of(investment));

        InvestmentResponse obtainedTransactionResponse = investmentService.getInvestment(investment.getId());

        verify(investmentRepository, times(1)).findById(investment.getId());
        assertNotNull(obtainedTransactionResponse);
        assertEquals(investment.getId(), obtainedTransactionResponse.getId());
        assertEquals(investment.getCurrentUserPrice(), obtainedTransactionResponse.getCurrentUserPrice());
        assertEquals(investment.getPurchasePrice(), obtainedTransactionResponse.getPurchasePrice());
        assertEquals(investment.getQuantity(), obtainedTransactionResponse.getQuantity());
        assertEquals(investment.getType(), obtainedTransactionResponse.getType());
    }

    @Test
    public void getInvestmentNotFound() {

        when(investmentRepository.findById(investment.getId())).thenReturn(Optional.empty());
        assertThrows(InvestmentNotFoundException.class, () -> investmentService.getInvestment(investmentRequest.getId()));
        verify(investmentRepository, times(1)).findById(investmentRequest.getId());
    }

    @Test
    public void getAllInvestmentsSuccessfully() {
        List<Investment> investments = List.of(investment, investment2);

        List<InvestmentResponse> expectedResponses = investments.stream()
                .map(inv -> new InvestmentResponse(
                        inv.getId(),
                        inv.getType(),
                        inv.getQuantity(),
                        inv.getPurchasePrice(),
                        inv.getCurrentUserPrice()))
                .toList();

        when(investmentRepository.findAll()).thenReturn(investments);
        List<InvestmentResponse> actualResponses = investmentService.getAllInvestment();

        for (int i = 0; i<expectedResponses.size(); i++) {
            InvestmentResponse expected = expectedResponses.get(i);
            InvestmentResponse actual = actualResponses.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getType(), actual.getType());
            assertEquals(expected.getPurchasePrice(), actual.getPurchasePrice());
            assertEquals(expected.getCurrentUserPrice(), actual.getCurrentUserPrice());
            assertEquals(expected.getQuantity(), actual.getQuantity());
        }

        verify(investmentRepository, times(1)).findAll();
    }

    @Test
    public void getAllIInvestmentEmptyList() {

        when(investmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<InvestmentResponse> actualResponses = investmentService.getAllInvestment();

        assertTrue(actualResponses.isEmpty());
        verify(investmentRepository, times(1)).findAll();
    }
}
