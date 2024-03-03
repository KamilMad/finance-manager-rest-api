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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestmentController.class)
public class InvestmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private InvestmentService investmentService;

    private InvestmentResponse investmentResponse;

    @BeforeEach
    public void init() {
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
        Mockito.when(investmentService.getInvestment(investmentId)).thenReturn(investmentResponse);

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
}
