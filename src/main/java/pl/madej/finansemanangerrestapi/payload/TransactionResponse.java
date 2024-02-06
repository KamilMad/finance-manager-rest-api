package pl.madej.finansemanangerrestapi.payload;

import pl.madej.finansemanangerrestapi.model.Category;
import pl.madej.finansemanangerrestapi.model.TransactionType;

public record TransactionResponse(
        long id,
        String description,
        double amount,
        TransactionType transactionType,
        Category category
) {
}
