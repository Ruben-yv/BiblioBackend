package com.example.BiblioBackend.service.impl;

import com.example.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import com.example.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import com.example.BiblioBackend.entity.Genero;
import com.example.BiblioBackend.entity.Libro;
import com.example.BiblioBackend.exception.ReglaNegocioException;
import com.example.BiblioBackend.repository.PrestamoRepository;
import com.example.BiblioBackend.service.service.ReporteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {
    private static final Logger log =
            LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final PrestamoRepository prestamoRepository;

    public ReporteServiceImpl(
            PrestamoRepository prestamoRepository) {

        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrestamoPorGeneroDTO> prestamosPorGenero(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte préstamos por género | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<PrestamoPorGeneroDTO> resultado = prestamoRepository
                .reportePrestamosPorGenero(
                        inicioDelDia(desde),
                        finDelDia(hasta));

        log.info("Fin reporte préstamos por género | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    @Transactional(readOnly = true)
    @Override
    public List<LibroMasPrestadoDTO> librosMasPrestados(
            LocalDate desde,
            LocalDate hasta) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio reporte libros más prestados | desde={} | hasta={}",
                desde, hasta);

        validarRango(desde, hasta);

        List<LibroMasPrestadoDTO> resultado = prestamoRepository
                .reporteLibrosMasPrestados(
                        inicioDelDia(desde),
                        finDelDia(hasta));

        log.info("Fin reporte libros más prestados | desde={} | hasta={} | "
                        + "filas={} | duracionMs={}",
                desde, hasta,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {

        if (desde != null
                && hasta != null
                && desde.isAfter(hasta)) {

            throw new ReglaNegocioException(
                    "El rango de fechas es inválido: 'desde' ("
                            + desde
                            + ") es posterior a 'hasta' ("
                            + hasta + ")");
        }
    }

    private LocalDateTime inicioDelDia(LocalDate fecha) {

        return (fecha == null)
                ? null
                : fecha.atStartOfDay();
    }

    private LocalDateTime finDelDia(LocalDate fecha) {

        return (fecha == null)
                ? null
                : fecha.atTime(LocalTime.MAX);
    }
}

