package com.example.BiblioBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrestamoResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private Long socioId;
    private String socioNombre;

    private String estado;
    private BigDecimal totalValorizado;

    private List<DetallePrestamoResponseDTO> detalles;
}