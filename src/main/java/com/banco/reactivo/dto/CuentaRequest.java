package com.banco.reactivo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es requerido")
    private String numeroCuenta;

    @NotBlank(message = "El titular es requerido")
    private String titular;

    @NotNull(message = "El saldo inicial es requerido")
    @Positive(message = "El saldo debe ser positivo")
    private BigDecimal saldo;

    @NotBlank(message = "El tipo de cuenta es requerido")
    private String tipoCuenta;
}