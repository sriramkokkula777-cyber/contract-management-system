package com.smartdocs.contractmanagement.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractResponse(
        Long id,
        String title,
        String vendor,
        ContractStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal value
) {
    public static ContractResponse from(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getTitle(),
                contract.getVendor(),
                contract.getStatus(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getValue()
        );
    }
}
