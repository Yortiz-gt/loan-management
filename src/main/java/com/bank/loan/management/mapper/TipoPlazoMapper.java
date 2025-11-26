package com.bank.loan.management.mapper;

import com.bank.loan.management.dto.TipoPlazoResponse;
import com.bank.loan.management.model.TipoPlazo;
import org.springframework.stereotype.Component;

@Component
public class TipoPlazoMapper {

    public TipoPlazoResponse toDto(TipoPlazo tipoPlazo) {
        if (tipoPlazo == null) {
            return null;
        }
        return new TipoPlazoResponse(tipoPlazo.getPlazoID(), tipoPlazo.getMeses(), tipoPlazo.getTasaInteres());
    }
}
