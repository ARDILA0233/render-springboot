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
@Table("cuentas")
public class Cuenta {

    @Id
    private Long id;

    @Column("numero_cuenta")
    private String numeroCuenta;

    private String titular;

    private BigDecimal saldo;

    @Column("tipo_cuenta")
    private String tipoCuenta; // AHORROS, CORRIENTE

    private String estado; // ACTIVA, INACTIVA, BLOQUEADA

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;


    public Cuenta(String numeroCuenta, String titular, BigDecimal saldo, String tipoCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.tipoCuenta = tipoCuenta;
        this.estado = "ACTIVA";
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
}