package com.example.BiblioBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class DetallePrestamoRequestDTO {
    @NotNull(
            message = "El libro es obligatorio"
    )
    @Positive(
            message = "El identificador del libro debe ser válido"
    )
    private Long libroId;

    @NotNull(
            message = "La cantidad es obligatoria"
    )
    @Min(
            value = 1,
            message = "La cantidad debe ser mayor que cero"
    )
    private Integer cantidad;
}