package com.example.restful.web.dto;

import com.example.restful.web.dto.validation.OnCreate;
import com.example.restful.web.dto.validation.OnDeduct;
import com.example.restful.web.dto.validation.OnDeposit;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountDto {

        private Long accountNumber;

        @NotBlank(message = "Name cannot be null or blank.",
                groups = OnCreate.class)
        private String name;

        @NotNull(message = "Amount cannot be nul.l",
                groups = {OnDeduct.class, OnDeposit.class})
        @DecimalMin(value = "0.00",
                inclusive = false,
                message = "Amount must be greater than 0.")
        private BigDecimal amount;

        @NotBlank(message = "Pin must be not null or blank.",
                groups = {OnDeduct.class, OnCreate.class})
        @Length(min = 4,
                max = 4,
                message = "Pin length must be 4 digits.",
                groups = {OnCreate.class, OnDeposit.class, OnDeduct.class})
        private String pin;
}
