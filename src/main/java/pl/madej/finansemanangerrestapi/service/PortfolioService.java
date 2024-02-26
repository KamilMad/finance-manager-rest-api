package pl.madej.finansemanangerrestapi.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.madej.finansemanangerrestapi.error.UnauthorizedAccessException;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.model.User;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final InvestmentService investmentService;
    private final UserService userService;

    public double calculateAggregatePortfolioValue() {

        User user = getAuthenticatedUser();

        return user.getInvestments()
                .stream()
                .mapToDouble(Investment::getCurrentUserPrice)
                .sum();
    }

    public Map<InvestmentType, Double> calculateGroupedPortfolioValue() {

        User user = getAuthenticatedUser();

        return user.getInvestments()
                .stream()
                .collect(Collectors.groupingBy(Investment::getType,
                        Collectors.summingDouble(Investment::getCurrentUserPrice)));

    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username);

    }
}
