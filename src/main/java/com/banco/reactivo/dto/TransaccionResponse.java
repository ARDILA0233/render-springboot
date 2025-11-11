package com.banco.reactivo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {

    private Long id;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;
    private String tipoTransaccion;
    private BigDecimal monto;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaTransaccion;
}