package pl.madej.finansemanangerrestapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.security.JwtService;
import pl.madej.finansemanangerrestapi.service.InvestmentService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestmentController.class)
public class InvestmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private InvestmentService investmentService;

    private InvestmentResponse investment1;
    private InvestmentResponse investment2;
    private InvestmentResponse investmentResponse;

    @BeforeEach
    public void init() {
        investment1 = new InvestmentResponse(1L, InvestmentType.STOCK, 10, 100.0, 150.0);
        investment2 = new InvestmentResponse(2L, InvestmentType.BOND, 5, 200.0, 250.0);

        investmentResponse = new InvestmentResponse(
                1L,
                InvestmentType.STOCK,
                10,
                100.0,
                150.0
        );
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void findInvestment_ShouldReturnInvestmentResponse() throws Exception {
        Long investmentId = 1L;
        when(investmentService.getInvestment(investmentId)).thenReturn(investmentResponse);

        mockMvc.perform(get("/api/v1/investment/{investmentId}", investmentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "type": "STOCK",
                            "quantity": 10,
                            "purchasePrice": 100.0,
                            "currentUserPrice": 150.0
                        }
                        """));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void findAllInvestment_ShouldReturnAllInvestments() throws Exception {
        List<InvestmentResponse> allInvestments = Arrays.asList(investment1, investment2);

        when(investmentService.getAllInvestment()).thenReturn(allInvestments);

        mockMvc.perform(get("/api/v1/investment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("STOCK"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].type").value("BOND"));
    }
}
