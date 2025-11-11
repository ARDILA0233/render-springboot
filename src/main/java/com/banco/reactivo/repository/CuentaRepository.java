package com.banco.reactivo.repository;

import com.banco.reactivo.entity.Cuenta;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaRepository extends ReactiveCrudRepository<Cuenta, Long> {

    Mono<Cuenta> findByNumeroCuenta(String numeroCuenta);

    @Query("SELECT * FROM cuentas WHERE estado = 'ACTIVA'")
    Flux<Cuenta> findCuentasActivas();
}