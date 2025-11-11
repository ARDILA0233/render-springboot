package com.banco.reactivo.service;

import com.banco.reactivo.entity.Cuenta;
import com.banco.reactivo.repository.CuentaRepository;
import com.banco.reactivo.dto.CuentaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public Flux<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }

    public Mono<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta no encontrada")));
    }

    public Mono<Cuenta> crearCuenta(CuentaRequest request) {
        return cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .flatMap(cuenta -> Mono.<Cuenta>error(
                        new RuntimeException("El número de cuenta ya existe")))
                .switchIfEmpty(Mono.defer(() -> {
                    Cuenta cuenta = new Cuenta(
                            request.getNumeroCuenta(),
                            request.getTitular(),
                            request.getSaldo(),
                            request.getTipoCuenta()
                    );
                    return cuentaRepository.save(cuenta);
                }));
    }

    public Mono<Cuenta> actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        return cuentaRepository.findById(cuentaId)
                .flatMap(cuenta -> {
                    cuenta.setSaldo(nuevoSaldo);
                    cuenta.setFechaActualizacion(LocalDateTime.now());
                    return cuentaRepository.save(cuenta);
                });
    }

    public Mono<Void> eliminarCuenta(Long id) {
        return cuentaRepository.deleteById(id);
    }
}