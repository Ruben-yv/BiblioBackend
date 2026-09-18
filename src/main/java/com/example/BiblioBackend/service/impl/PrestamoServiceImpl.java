package com.example.BiblioBackend.service.impl;

import com.example.BiblioBackend.dto.DetallePrestamoRequestDTO;
import com.example.BiblioBackend.dto.DetallePrestamoResponseDTO;
import com.example.BiblioBackend.dto.PrestamoRequestDTO;
import com.example.BiblioBackend.dto.PrestamoResponseDTO;
import com.example.BiblioBackend.entity.DetallePrestamo;
import com.example.BiblioBackend.entity.Libro;
import com.example.BiblioBackend.entity.Prestamo;
import com.example.BiblioBackend.entity.Socio;
import com.example.BiblioBackend.enums.EstadoPrestamo;
import com.example.BiblioBackend.exception.RecursoNoEncontradoException;
import com.example.BiblioBackend.exception.ReglaNegocioException;
import com.example.BiblioBackend.repository.LibroRepository;
import com.example.BiblioBackend.repository.PrestamoRepository;
import com.example.BiblioBackend.repository.SocioRepository;
import com.example.BiblioBackend.service.service.PrestamoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    private static final Logger log = LoggerFactory.getLogger(PrestamoServiceImpl.class);

    private final PrestamoRepository prestamoRepository;
    private final SocioRepository socioRepository;
    private final LibroRepository libroRepository;

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "fecha", "totalValorizado", "estado");
    private static final String ORDEN_POR_DEFECTO = "fecha";

    public PrestamoServiceImpl(
            PrestamoRepository prestamoRepository,
            SocioRepository socioRepository,
            LibroRepository libroRepository) {

        this.prestamoRepository = prestamoRepository;
        this.socioRepository = socioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public PrestamoResponseDTO registrar(PrestamoRequestDTO request) {
        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con id: " + request.getSocioId()));

        if (!Boolean.TRUE.equals(socio.getEstado())) {
            throw new ReglaNegocioException("No se puede registrar un préstamo para un socio inactivo");
        }
        Prestamo prestamo = new Prestamo();

        prestamo.setSocio(socio);
        prestamo.setFecha(LocalDateTime.now());
        prestamo.setEstado(EstadoPrestamo.REGISTRADO);

        BigDecimal totalValorizado = BigDecimal.ZERO;

        for (DetallePrestamoRequestDTO item : request.getDetalles()) {
            Libro libro = libroRepository.findById(item.getLibroId()).orElseThrow(() ->
                    new RecursoNoEncontradoException("Libro no encontrado con id: " + item.getLibroId()));

            if (!Boolean.TRUE.equals(libro.getEstado())) {
                throw new ReglaNegocioException("El libro " + libro.getTitulo() + " se encuentra inactivo");
            }

            if (libro.getStock() < item.getCantidad()) {

                throw new ReglaNegocioException("Stock insuficiente para " + libro.getTitulo() + ". Disponible: " + libro.getStock()
                        + ", solicitado: " + item.getCantidad());
            }

            BigDecimal subtotal = libro.getCostoReposicion().multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePrestamo detalle = new DetallePrestamo();

            detalle.setLibro(libro);
            detalle.setCantidad(item.getCantidad());
            detalle.setCostoUnitario(libro.getCostoReposicion());
            detalle.setSubtotal(subtotal);

            prestamo.agregarDetalle(detalle);

            totalValorizado = totalValorizado.add(subtotal);

            libro.setStock(libro.getStock() - item.getCantidad());
        }

        prestamo.setTotalValorizado(totalValorizado);

        Prestamo guardado = prestamoRepository.save(prestamo);

        return convertirResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO buscar(Long id) {

        Prestamo prestamo = prestamoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Préstamo no encontrado con id: " + id));
        return convertirResponse(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listar() {
        return prestamoRepository.findAll().stream().map(this::convertirResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrestamoResponseDTO> buscarPrestamos(
            Long socioId,
            EstadoPrestamo estadoPrestamo,
            LocalDate desde,
            LocalDate hasta,
            String ordenarPor,
            String direccion) {

        long inicio = System.currentTimeMillis();

        log.info("Inicio buscar préstamos | socioId={} | estado={} | "
                        + "desde={} | hasta={} | ordenarPor={} | direccion={}",
                socioId, estadoPrestamo, desde, hasta, ordenarPor, direccion);

        if (desde != null
                && hasta != null
                && desde.isAfter(hasta)) {

            throw new ReglaNegocioException(
                    "El rango de fechas es inválido: 'desde' ("
                            + desde
                            + ") es posterior a 'hasta' ("
                            + hasta + ")");
        }

        Sort sort = construirSort(ordenarPor, direccion);

        LocalDateTime desdeHora = (desde == null)
                ? null
                : desde.atStartOfDay();

        LocalDateTime hastaHora = (hasta == null)
                ? null
                : hasta.atTime(LocalTime.MAX);

        List<PrestamoResponseDTO> resultado =
                prestamoRepository.findAll(sort)
                        .stream()
                        .filter(prestamo -> socioId == null || prestamo.getSocio().getId().equals(socioId))
                        .filter(prestamo -> estadoPrestamo == null || prestamo.getEstado() == estadoPrestamo)
                        .filter(prestamo -> desdeHora == null || !prestamo.getFecha().isBefore(desdeHora))
                        .filter(prestamo -> hastaHora == null || !prestamo.getFecha().isAfter(hastaHora))
                        .map(this::convertirResponse)
                        .toList();

        log.info("Fin buscar préstamos | socioId={} | estado={} | "
                        + "desde={} | hasta={} | orden={} {} | "
                        + "filas={} | duracionMs={}",
                socioId, estadoPrestamo, desde, hasta, ordenarPor, direccion,
                resultado.size(),
                System.currentTimeMillis() - inicio);

        return resultado;
    }

    private Sort construirSort(String ordenarPor, String direccion) {

        String campo = (ordenarPor == null || ordenarPor.isBlank())
                ? ORDEN_POR_DEFECTO
                : ordenarPor.trim();

        if (!CAMPOS_ORDENABLES.contains(campo)) {

            throw new ReglaNegocioException(
                    "El campo de ordenamiento '"
                            + campo
                            + "' no está permitido. Campos válidos: "
                            + CAMPOS_ORDENABLES);
        }

        String sentido = (direccion == null || direccion.isBlank())
                ? "desc"
                : direccion.trim();

        if (!sentido.equalsIgnoreCase("asc")
                && !sentido.equalsIgnoreCase("desc")) {

            throw new ReglaNegocioException(
                    "La dirección de ordenamiento '"
                            + sentido
                            + "' no está permitida. Valores válidos: asc, desc");
        }

        return sentido.equalsIgnoreCase("asc")
                ? Sort.by(campo).ascending()
                : Sort.by(campo).descending();
    }

    private PrestamoResponseDTO convertirResponse(Prestamo prestamo) {

        List<DetallePrestamoResponseDTO> detalles =
                prestamo.getDetalles()
                        .stream()
                        .map(detalle ->
                                new DetallePrestamoResponseDTO(
                                        detalle.getId(),
                                        detalle.getLibro().getId(),
                                        detalle.getLibro().getTitulo(),
                                        detalle.getCantidad(),
                                        detalle.getCostoUnitario(),
                                        detalle.getSubtotal()
                                )
                        ).toList();

        String socioNombre = prestamo.getSocio().getNombres() + " " + prestamo.getSocio().getApellidos();

        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getFecha(),
                prestamo.getSocio().getId(),
                socioNombre,
                prestamo.getEstado().name(),
                prestamo.getTotalValorizado(),
                detalles
        );
    }
}
