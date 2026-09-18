package com.example.BiblioBackend.entity;

import com.example.BiblioBackend.enums.EstadoPrestamo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name = "prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_devolucion_previa", nullable = false)
    private LocalDate fechaDevolucionPrevista;

    @Column(name = "fecha_devolucion_real", nullable = true)
    private LocalDate fechaDevolucionReal;

    @Column(name = "total_valorizado", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalValorizado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "socio_id",
            nullable = false
    )
    private Socio socio;

    @OneToMany(
            mappedBy = "prestamo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetallePrestamo> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (fecha == null) {
            fecha = LocalDateTime.now();
        }

        if (estado == null) {
            estado = EstadoPrestamo.REGISTRADO;
        }
    }

    public void agregarDetalle(DetallePrestamo detalle) {
        detalles.add(detalle);
        detalle.setPrestamo(this);
    }

}
