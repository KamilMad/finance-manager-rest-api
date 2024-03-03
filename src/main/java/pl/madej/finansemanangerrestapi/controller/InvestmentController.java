package pl.madej.finansemanangerrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.service.InvestmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investment")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping("/{investmentId}")
    public ResponseEntity<InvestmentResponse> findInvestment(@PathVariable Long investmentId) {

        InvestmentResponse investmentResponse = investmentService.getInvestment(investmentId);
        return ResponseEntity.ok(investmentResponse);
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponse>> findAllInvestment() {
        List<InvestmentResponse> investments = investmentService.getAllInvestment();

        return ResponseEntity.ok(investments);
    }

    @PostMapping
    public ResponseEntity<Long> addInvestment(@RequestBody InvestmentRequest investmentRequest) {
        Long investmentId = investmentService.addInvestment(investmentRequest);
        return ResponseEntity.ok(investmentId);
    }

    @DeleteMapping("/{investmentId}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long investmentId) {
        investmentService.deleteInvestment(investmentId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{investmentId}")
    public ResponseEntity<InvestmentResponse> updateInvestment(@PathVariable Long investmentId, @RequestBody InvestmentRequest investmentRequest) {
        InvestmentResponse investmentResponse = investmentService.updateInvestment(investmentId, investmentRequest);

        return ResponseEntity.ok(investmentResponse);
    }

    @GetMapping("/{investmentId}/performance")
    public ResponseEntity<String> investmentROI(@PathVariable Long id) {

        return null;
    }

}
