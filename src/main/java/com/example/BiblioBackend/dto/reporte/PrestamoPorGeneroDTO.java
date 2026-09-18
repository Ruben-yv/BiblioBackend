package com.example.BiblioBackend.dto.reporte;

import java.math.BigDecimal;

public record PrestamoPorGeneroDTO(
        Long generoId,
        String generoNombre,
        Long cantidadPrestamos,
        BigDecimal totalValorizado
) {
}
