package pl.madej.finansemanangerrestapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionRequest;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction toTransaction(TransactionRequest transactionRequest);
    TransactionResponse toTransactionResponse(Transaction transaction);
    void updateTransactionFromDto(TransactionRequest transactionRequest, @MappingTarget Transaction transaction);


}
