package com.example.BiblioBackend.controller;

import com.example.BiblioBackend.dto.PrestamoRequestDTO;
import com.example.BiblioBackend.dto.PrestamoResponseDTO;
import com.example.BiblioBackend.enums.EstadoPrestamo;
import com.example.BiblioBackend.service.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(
            PrestamoService prestamoService) {

        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrar(
            @Valid
            @RequestBody PrestamoRequestDTO request) {

        PrestamoResponseDTO response =
                prestamoService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.buscar(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listar() {

        return ResponseEntity.ok(
                prestamoService.listar()
        );
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<PrestamoResponseDTO>> buscar(

            @RequestParam(required = false)
            Long socioId,

            @RequestParam(required = false)
            EstadoPrestamo estado,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta,

            @RequestParam(required = false, defaultValue = "fecha")
            String ordenarPor,

            @RequestParam(required = false, defaultValue = "desc")
            String direccion) {

        return ResponseEntity.ok(
                prestamoService.buscarPrestamos(
                        socioId,
                        estado,
                        desde,
                        hasta,
                        ordenarPor,
                        direccion
                )
        );
    }
}
