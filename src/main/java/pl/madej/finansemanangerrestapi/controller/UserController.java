package pl.madej.finansemanangerrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.madej.finansemanangerrestapi.service.PortfolioService;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final PortfolioService portfolioService;

    @GetMapping("/portfolio_value")
    public ResponseEntity<?> getInvestmentValue(@RequestParam(required = false) String type) {

        if ("grouped".equals(type)) {
            return ResponseEntity.ok().body(portfolioService.calculateGroupedPortfolioValue());
        } else {
            return ResponseEntity.ok().body(portfolioService.calculateAggregatePortfolioValue());
        }
    }
}
