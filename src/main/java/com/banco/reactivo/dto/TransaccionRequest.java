package com.banco.reactivo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionRequest {

    private Long cuentaOrigenId;

    private Long cuentaDestinoId;

    @NotNull(message = "El tipo de transacción es requerido")
    private String tipoTransaccion;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal monto;

    private String descripcion;
}