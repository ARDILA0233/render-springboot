package com.banco.reactivo.service;

import com.banco.reactivo.dto.TransaccionRequest;
import com.banco.reactivo.dto.TransaccionResponse;
import com.banco.reactivo.entity.Cuenta;
import com.banco.reactivo.entity.Transaccion;
import com.banco.reactivo.repository.CuentaRepository;
import com.banco.reactivo.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final CuentaService cuentaService;

    public Flux<TransaccionResponse> obtenerTodasLasTransacciones() {
        return transaccionRepository.findUltimasTransacciones()
                .flatMap(this::convertirAResponse);
    }

    public Flux<TransaccionResponse> obtenerTransaccionesPorCuenta(Long cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId)
                .flatMap(this::convertirAResponse);
    }

    public Mono<TransaccionResponse> realizarTransaccion(TransaccionRequest request) {
        return switch (request.getTipoTransaccion()) {
            case "DEPOSITO" -> realizarDeposito(request);
            case "RETIRO" -> realizarRetiro(request);
            case "TRANSFERENCIA" -> realizarTransferencia(request);
            default -> Mono.error(new RuntimeException("Tipo de transacción no válido"));
        };
    }

    private Mono<TransaccionResponse> realizarDeposito(TransaccionRequest request) {
        return cuentaService.obtenerCuentaPorId(request.getCuentaDestinoId())
                .flatMap(cuenta -> {
                    BigDecimal nuevoSaldo = cuenta.getSaldo().add(request.getMonto());
                    return cuentaService.actualizarSaldo(cuenta.getId(), nuevoSaldo)
                            .then(guardarTransaccion(null, cuenta.getId(), "DEPOSITO",
                                    request.getMonto(), request.getDescripcion()));
                })
                .flatMap(this::convertirAResponse);
    }

    private Mono<TransaccionResponse> realizarRetiro(TransaccionRequest request) {
        return cuentaService.obtenerCuentaPorId(request.getCuentaOrigenId())
                .flatMap(cuenta -> {
                    if (cuenta.getSaldo().compareTo(request.getMonto()) < 0) {
                        return Mono.error(new RuntimeException("Saldo insuficiente"));
                    }
                    BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(request.getMonto());
                    return cuentaService.actualizarSaldo(cuenta.getId(), nuevoSaldo)
                            .then(guardarTransaccion(cuenta.getId(), null, "RETIRO",
                                    request.getMonto(), request.getDescripcion()));
                })
                .flatMap(this::convertirAResponse);
    }

    private Mono<TransaccionResponse> realizarTransferencia(TransaccionRequest request) {
        return Mono.zip(
                cuentaService.obtenerCuentaPorId(request.getCuentaOrigenId()),
                cuentaService.obtenerCuentaPorId(request.getCuentaDestinoId())
        ).flatMap(tuple -> {
            Cuenta origen = tuple.getT1();
            Cuenta destino = tuple.getT2();

            if (origen.getSaldo().compareTo(request.getMonto()) < 0) {
                return Mono.error(new RuntimeException("Saldo insuficiente"));
            }

            BigDecimal nuevoSaldoOrigen = origen.getSaldo().subtract(request.getMonto());
            BigDecimal nuevoSaldoDestino = destino.getSaldo().add(request.getMonto());

            return cuentaService.actualizarSaldo(origen.getId(), nuevoSaldoOrigen)
                    .then(cuentaService.actualizarSaldo(destino.getId(), nuevoSaldoDestino))
                    .then(guardarTransaccion(origen.getId(), destino.getId(),
                            "TRANSFERENCIA", request.getMonto(), request.getDescripcion()));
        }).flatMap(this::convertirAResponse);
    }

    private Mono<Transaccion> guardarTransaccion(Long origenId, Long destinoId,
                                                 String tipo, BigDecimal monto, String desc) {
        Transaccion transaccion = new Transaccion(origenId, destinoId, tipo, monto, desc);
        return transaccionRepository.save(transaccion);
    }

    private Mono<TransaccionResponse> convertirAResponse(Transaccion t) {
        Mono<String> numOrigen = t.getCuentaOrigenId() != null
                ? cuentaRepository.findById(t.getCuentaOrigenId())
                .map(Cuenta::getNumeroCuenta)
                .defaultIfEmpty("N/A")
                : Mono.just("N/A");

        Mono<String> numDestino = t.getCuentaDestinoId() != null
                ? cuentaRepository.findById(t.getCuentaDestinoId())
                .map(Cuenta::getNumeroCuenta)
                .defaultIfEmpty("N/A")
                : Mono.just("N/A");

        return Mono.zip(numOrigen, numDestino)
                .map(tuple -> new TransaccionResponse(
                        t.getId(),
                        tuple.getT1(),
                        tuple.getT2(),
                        t.getTipoTransaccion(),
                        t.getMonto(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getFechaTransaccion()
                ));
    }
}