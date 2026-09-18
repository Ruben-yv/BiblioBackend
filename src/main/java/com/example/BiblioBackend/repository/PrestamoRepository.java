package com.example.BiblioBackend.repository;

import com.example.BiblioBackend.dto.PrestamoResponseDTO;
import com.example.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import com.example.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import com.example.BiblioBackend.entity.Prestamo;
import com.example.BiblioBackend.enums.EstadoPrestamo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query(
            """
            SELECT DISTINCT p FROM Prestamo p
            LEFT JOIN FETCH p.socio s
            LEFT JOIN FETCH p.detalles d
            LEFT JOIN FETCH d.libro l
            WHERE (:socioId IS NULL OR s.id = :socioId)
             AND (:estado IS NULL OR p.estado = :estado)
             AND (:desde IS NULL OR p.fecha >= :desde)
             AND (:hasta IS NULL OR p.fecha <= :hasta)
            """
    )
    List<Prestamo> buscar(
            @Param("socioId") Long socioId,
            @Param("estado") EstadoPrestamo estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Sort sort
    );

    @Query("""
            select new com.example.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO(
                       g.id,
                       g.nombre,
                       sum(d.cantidad),
                       sum(d.subtotal))
            from DetallePrestamo d
            join d.prestamo p
            join d.libro l
            join l.genero g
            where p.estado = com.example.BiblioBackend.enums.EstadoPrestamo.REGISTRADO
              and (:desde is null or p.fecha >= :desde)
              and (:hasta is null or p.fecha <= :hasta)
            group by g.id, g.nombre
            order by sum(d.subtotal) desc
            """)
    List<PrestamoPorGeneroDTO> reportePrestamosPorGenero(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    @Query("""
            select new com.example.BiblioBackend.dto.reporte.LibroMasPrestadoDTO(
                       l.id,
                       l.titulo,
                       sum(d.cantidad),
                       sum(d.subtotal))
            from DetallePrestamo d
            join d.prestamo p
            join d.libro l
            where p.estado = com.example.BiblioBackend.enums.EstadoPrestamo.REGISTRADO
              and (:desde is null or p.fecha >= :desde)
              and (:hasta is null or p.fecha <= :hasta)
            group by l.id, l.titulo
            order by sum(d.cantidad) desc
            """)
    List<LibroMasPrestadoDTO> reporteLibrosMasPrestados(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}


