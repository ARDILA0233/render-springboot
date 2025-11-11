package com.banco.reactivo.controller;

import com.banco.reactivo.dto.CuentaRequest;
import com.banco.reactivo.entity.Cuenta;
import com.banco.reactivo.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public Flux<Cuenta> obtenerTodasLasCuentas() {
        return cuentaService.obtenerTodasLasCuentas()
                .log();
    }

    @GetMapping("/{id}")
    public Mono<Cuenta> obtenerCuentaPorId(@PathVariable Long id) {
        return cuentaService.obtenerCuentaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cuenta> crearCuenta(@Valid @RequestBody CuentaRequest request) {
        return cuentaService.crearCuenta(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarCuenta(@PathVariable Long id) {
        return cuentaService.eliminarCuenta(id);
    }
}