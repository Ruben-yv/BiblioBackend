package com.example.BiblioBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetallePrestamoResponseDTO {
    private Long detalleId;
    private Long libroId;
    private String libroTitulo;
    private Integer cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal subtotal;
}