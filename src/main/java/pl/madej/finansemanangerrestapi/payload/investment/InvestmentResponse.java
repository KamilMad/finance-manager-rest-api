package pl.madej.finansemanangerrestapi.payload.investment;

import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;

public record InvestmentResponse(
        InvestmentType type,
        int quantity,
        double purchasePrice,
        double currentUserPrice
) {
}
