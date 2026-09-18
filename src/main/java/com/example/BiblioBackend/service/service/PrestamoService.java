package com.example.BiblioBackend.service.service;

import com.example.BiblioBackend.dto.PrestamoRequestDTO;
import com.example.BiblioBackend.dto.PrestamoResponseDTO;
import com.example.BiblioBackend.enums.EstadoPrestamo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO registrar(PrestamoRequestDTO request);
    PrestamoResponseDTO buscar(Long id);
    List<PrestamoResponseDTO> listar();
    List<PrestamoResponseDTO> buscarPrestamos(
            Long socioId,
            EstadoPrestamo estadoPrestamo,
            LocalDate desde,
            LocalDate hasta,
            String ordenarPor,
            String direccion);
}
