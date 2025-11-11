package com.banco.reactivo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("transacciones")
public class Transaccion {

    @Id
    private Long id;

    @Column("cuenta_origen_id")
    private Long cuentaOrigenId;

    @Column("cuenta_destino_id")
    private Long cuentaDestinoId;

    @Column("tipo_transaccion")
    private String tipoTransaccion;

    private BigDecimal monto;

    private String descripcion;

    private String estado;

    @Column("fecha_transaccion")
    private LocalDateTime fechaTransaccion;

    public Transaccion(Long cuentaOrigenId, Long cuentaDestinoId, String tipoTransaccion,
                       BigDecimal monto, String descripcion) {
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.estado = "COMPLETADA";
        this.fechaTransaccion = LocalDateTime.now();
    }
}