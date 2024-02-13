package pl.madej.finansemanangerrestapi.mapper;

import org.mapstruct.factory.Mappers;
import pl.madej.finansemanangerrestapi.model.Investment;
import pl.madej.finansemanangerrestapi.payload.investment.InvestmentRequest;

public interface InvestmentMapper {

    InvestmentMapper INSTANCE = Mappers.getMapper(InvestmentMapper.class);

    Investment toInvestment(InvestmentRequest investmentRequest);

}
