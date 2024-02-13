package pl.madej.finansemanangerrestapi.payload.transaction;

import pl.madej.finansemanangerrestapi.model.enums.Category;
import pl.madej.finansemanangerrestapi.model.enums.TransactionType;

public record TransactionResponse(
        long id,
        String description,
        double amount,
        TransactionType transactionType,
        Category category
) {
}
