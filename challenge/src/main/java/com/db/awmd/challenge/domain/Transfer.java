package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Devendra Singh Sengar on 14-03-2018.
 */
@Data
public class Transfer {
    @NotNull
    @NotEmpty
    private final String accountFrom;

    @NotNull
    @NotEmpty
    private final String accountTo;

    @NotNull
    @Min(value = 1, message = "Transfer amount must be positive.")
    private BigDecimal amount;

    public Transfer(String accountFrom, String accountTo) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = BigDecimal.ZERO;
    }

    @JsonCreator
    public Transfer(@JsonProperty("accountFrom") String accountFrom,
                   @JsonProperty("accountTo") String accountTo,
                   @JsonProperty("amount") BigDecimal amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

}
