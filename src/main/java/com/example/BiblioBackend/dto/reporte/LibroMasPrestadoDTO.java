package com.example.BiblioBackend.dto.reporte;

import java.math.BigDecimal;

public record LibroMasPrestadoDTO(
        Long libroId,
        String libroTitulo,
        Long cantidadPrestada,
        BigDecimal totalValorizado
) {
}
