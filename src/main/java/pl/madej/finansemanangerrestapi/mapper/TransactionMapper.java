package pl.madej.finansemanangerrestapi.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.payload.TransactionRequest;

public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction toTransaction(TransactionRequest transactionRequest);
}
