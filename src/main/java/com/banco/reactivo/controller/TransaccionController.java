package com.banco.reactivo.controller;

import com.banco.reactivo.dto.TransaccionRequest;
import com.banco.reactivo.dto.TransaccionResponse;
import com.banco.reactivo.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping
    public Flux<TransaccionResponse> obtenerTodasLasTransacciones() {
        return transaccionService.obtenerTodasLasTransacciones()
                .log();
    }

    @GetMapping("/cuenta/{cuentaId}")
    public Flux<TransaccionResponse> obtenerTransaccionesPorCuenta(@PathVariable Long cuentaId) {
        return transaccionService.obtenerTransaccionesPorCuenta(cuentaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransaccionResponse> realizarTransaccion(@Valid @RequestBody TransaccionRequest request) {
        return transaccionService.realizarTransaccion(request);
    }
}