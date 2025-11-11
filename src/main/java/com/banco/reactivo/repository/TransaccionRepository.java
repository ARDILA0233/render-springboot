package com.banco.reactivo.repository;

import com.banco.reactivo.entity.Transaccion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TransaccionRepository extends ReactiveCrudRepository<Transaccion, Long> {

    @Query("SELECT * FROM transacciones WHERE cuenta_origen_id = :cuentaId OR cuenta_destino_id = :cuentaId ORDER BY fecha_transaccion DESC")
    Flux<Transaccion> findByCuentaId(Long cuentaId);

    @Query("SELECT * FROM transacciones ORDER BY fecha_transaccion DESC LIMIT 10")
    Flux<Transaccion> findUltimasTransacciones();
}