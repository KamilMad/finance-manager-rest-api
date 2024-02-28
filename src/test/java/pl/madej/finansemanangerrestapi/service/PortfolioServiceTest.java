package pl.madej.finansemanangerrestapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private PortfolioService portfolioService;

    private User user;
    private List<Investment> investments;

    @BeforeEach
    public void init() {
        user = new User(1L, "username", "password", "email", Collections.emptyList(), Collections.emptyList());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.setContext(context);

        investments = createTestInvestments();
        user.setInvestments(investments);
    }

    @Test
    public void calculateAggregatePortfolioValueSuccessful() {

        when(userService.getUserByUsername("username")).thenReturn(user);
        double sum = portfolioService.calculateAggregatePortfolioValue();
        assertEquals(15000.0, sum);

    }

    @Test
    public void calculateGroupedPortfolioValue() {
        when(userService.getUserByUsername("username")).thenReturn(user);
        Map<InvestmentType, Double> groupedPortfolioValue = portfolioService.calculateGroupedPortfolioValue();

        assertEquals(6000.0, groupedPortfolioValue.get(InvestmentType.STOCK));
        assertEquals(4000.0, groupedPortfolioValue.get(InvestmentType.BOND));
        assertEquals(5000.0, groupedPortfolioValue.get(InvestmentType.CRYPTOCURRENCY));

    }

    public List<Investment> createTestInvestments() {
        Investment investment1 = new Investment();
        investment1.setId(1L);
        investment1.setType(InvestmentType.STOCK);
        investment1.setQuantity(10);
        investment1.setPurchasePrice(50.00);
        investment1.setCurrentUserPrice(100.00);
        investment1.setUser(user);

        Investment investment2 = new Investment();
        investment2.setId(2L);
        investment2.setType(InvestmentType.STOCK);
        investment2.setQuantity(10);
        investment2.setPurchasePrice(100.00);
        investment2.setCurrentUserPrice(200.00);
        investment2.setUser(user);

        Investment investment3 = new Investment();
        investment3.setId(3L);
        investment3.setType(InvestmentType.STOCK);
        investment3.setQuantity(10);
        investment3.setPurchasePrice(75.00);
        investment3.setCurrentUserPrice(300.00);
        investment3.setUser(user);

        Investment investment4 = new Investment();
        investment4.setId(4L);
        investment4.setType(InvestmentType.BOND);
        investment4.setQuantity(10);
        investment4.setPurchasePrice(200.00);
        investment4.setCurrentUserPrice(400.00);
        investment4.setUser(user);

        Investment investment5 = new Investment();
        investment5.setId(5L);
        investment5.setType(InvestmentType.CRYPTOCURRENCY);
        investment5.setQuantity(10);
        investment5.setPurchasePrice(300.00);
        investment5.setCurrentUserPrice(500.00);
        investment5.setUser(user);

        return List.of(investment1, investment2, investment3, investment4, investment5);
    }
}