package pl.madej.finansemanangerrestapi.payload.investment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.madej.finansemanangerrestapi.model.enums.InvestmentType;

@Getter
@Setter
@AllArgsConstructor
public class InvestmentRequest {
    private long id;
    private InvestmentType type;
    private int quantity;
    private double purchasePrice;
    private double currentUserPrice;


}
