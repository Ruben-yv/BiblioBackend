package com.example.BiblioBackend.controller;

import com.example.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import com.example.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import com.example.BiblioBackend.service.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {
    private final ReporteService reporteService;

    public ReporteController(
            ReporteService reporteService) {

        this.reporteService = reporteService;
    }

    @GetMapping("/prestamos-por-genero")
    public ResponseEntity<List<PrestamoPorGeneroDTO>> prestamosPorGenero(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        return ResponseEntity.ok(
                reporteService.prestamosPorGenero(desde, hasta)
        );
    }

    @GetMapping("/libros-mas-prestados")
    public ResponseEntity<List<LibroMasPrestadoDTO>> librosMasPrestados(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta) {

        return ResponseEntity.ok(
                reporteService.librosMasPrestados(desde, hasta)
        );
    }
}
