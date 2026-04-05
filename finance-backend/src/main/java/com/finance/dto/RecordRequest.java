package com.finance.dto;

import com.finance.model.FinancialRecord;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordRequest {
    @NotNull @Positive
    private BigDecimal amount;

    @NotNull
    private FinancialRecord.Type type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;
}
