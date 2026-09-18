package com.example.BiblioBackend.service.service;

import com.example.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import com.example.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    @Transactional(readOnly = true)
    List<PrestamoPorGeneroDTO> prestamosPorGenero(
            LocalDate desde,
            LocalDate hasta);

    @Transactional(readOnly = true)
    List<LibroMasPrestadoDTO> librosMasPrestados(
            LocalDate desde,
            LocalDate hasta);
}

