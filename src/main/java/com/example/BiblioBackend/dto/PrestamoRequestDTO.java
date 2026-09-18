package com.example.BiblioBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrestamoRequestDTO {
    @NotNull(
            message = "El socio es obligatorio"
    )
    @Positive(
            message = "El identificador del socio debe ser válido"
    )
    private Long socioId;

    @NotEmpty(
            message = "La venta debe contener al menos un detalle"
    )
    @Valid
    private List<DetallePrestamoRequestDTO> detalles;
}