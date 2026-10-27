package com.smartdocs.contractmanagement.contract;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 150) String vendor,
        @NotNull ContractStatus status,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal value
) {
}
