package pl.madej.finansemanangerrestapi.mapper;

import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.model.Transaction;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentResponse;
import pl.madej.finansemanangerrestapi.payload.transaction.TransactionRequest;

public interface InvestmentMapper {

    InvestmentMapper INSTANCE = Mappers.getMapper(InvestmentMapper.class);

    Investment toInvestment(InvestmentRequest investmentRequest);
    InvestmentResponse toInvestmentResponse(Investment investment);
    void updateTransactionFromDto(InvestmentRequest investmentRequest, @MappingTarget Investment investment);

}
