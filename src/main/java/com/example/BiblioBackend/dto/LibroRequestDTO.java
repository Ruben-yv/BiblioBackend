package com.example.BiblioBackend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LibroRequestDTO {
    @NotBlank(message = "El nombre del libro es obligatorio")
    @Size(
            min = 3,
            max = 150,
            message = "El nombre debe tener entre 3 y 150 caracteres"
    )
    private String titulo;
    @NotBlank(message = "El autor es obligatorio")
    @Size(
            max = 120,
            message = "el nombre del autor no debe superar los 150 caracteres"
    )
    private String autor;

    @NotBlank(message = "El isbn es obligatorio")
    @Pattern(regexp = "\\d{10}|\\d{13}",
            message = "El DNI debe contener exactamente 10 o 13 dígitos")
    private String isbn;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
    @NotNull(message = "El costo reposicion es obligatorio")
    @DecimalMin(value = "0.01")
    private BigDecimal costoReposicion;
    @NotNull(message = "El stock es obligatorio")
    @Min(
            value = 1
    )
    private int stock;
    @NotNull(message = "El id del genero es obligatorio")
    @Positive
    private Long generoId;
}