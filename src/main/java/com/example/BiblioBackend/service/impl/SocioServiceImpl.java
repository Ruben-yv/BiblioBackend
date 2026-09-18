package com.example.BiblioBackend.service.impl;

import com.example.BiblioBackend.dto.SocioRequestDTO;
import com.example.BiblioBackend.dto.SocioResponseDTO;
import com.example.BiblioBackend.entity.Socio;
import com.example.BiblioBackend.exception.RecursoNoEncontradoException;
import com.example.BiblioBackend.exception.ReglaNegocioException;
import com.example.BiblioBackend.repository.SocioRepository;
import com.example.BiblioBackend.service.service.SocioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SocioServiceImpl implements SocioService {
    private static final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    @Transactional
    public SocioResponseDTO create(
            SocioRequestDTO request) {

        log.info(
                "Registrando socio con DNI={}",
                request.getDni()
        );

        String dni = request.getDni().trim();
        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (socioRepository.existsByDni(dni)) {
            throw new ReglaNegocioException(
                    "Ya existe un socio con el DNI: " + dni
            );
        }

        if (socioRepository.existsByEmailIgnoreCase(email)) {
            throw new ReglaNegocioException(
                    "Ya existe un socio con el correo: " + email
            );
        }

        Socio socio = new Socio();

        socio.setDni(dni);
        socio.setNombres(
                request.getNombres().trim()
        );
        socio.setApellidos(
                request.getApellidos().trim()
        );
        socio.setEmail(email);
        socio.setTelefono(
                normalizar(request.getTelefono())
        );
        socio.setDireccion(
                normalizar(request.getDireccion())
        );
        socio.setEstado(request.getEstado());

        Socio guardado =
                socioRepository.save(socio);

        log.info(
                "Socio registrado correctamente id={}",
                guardado.getId()
        );

        return convertirResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponseDTO read(Long id) {

        log.info("Buscando socio id={}", id);

        Socio socio =
                socioRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Socio no encontrado con id: " + id
                                )
                        );

        return convertirResponse(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponseDTO> readAll() {

        log.info("Listando socios");

        return socioRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional
    public SocioResponseDTO update(
            Long id,
            SocioRequestDTO request) {

        Socio socio =
                socioRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Socio no encontrado con id: " + id
                                )
                        );

        String dni = request.getDni().trim();
        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (socioRepository
                .existsByDniAndIdNot(dni, id)) {

            throw new ReglaNegocioException(
                    "Ya existe otro socio con el DNI: "
                            + dni
            );
        }

        if (socioRepository
                .existsByEmailIgnoreCaseAndIdNot(
                        email,
                        id)) {

            throw new ReglaNegocioException(
                    "Ya existe otro socio con el correo: "
                            + email
            );
        }

        socio.setDni(dni);
        socio.setNombres(
                request.getNombres().trim()
        );
        socio.setApellidos(
                request.getApellidos().trim()
        );
        socio.setEmail(email);
        socio.setTelefono(
                normalizar(request.getTelefono())
        );
        socio.setDireccion(
                normalizar(request.getDireccion())
        );
        socio.setEstado(request.getEstado());

        Socio actualizado =
                socioRepository.save(socio);

        log.info(
                "Socio id={} actualizado correctamente",
                id
        );

        return convertirResponse(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Socio socio =
                socioRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Socio no encontrado con id: " + id
                                )
                        );

        socioRepository.delete(socio);

        log.info(
                "Socio id={} eliminado correctamente",
                id
        );
    }

    private SocioResponseDTO convertirResponse(
            Socio socio) {

        return new SocioResponseDTO(
                socio.getId(),
                socio.getDni(),
                socio.getNombres(),
                socio.getApellidos(),
                socio.getEmail(),
                socio.getTelefono(),
                socio.getDireccion(),
                socio.getEstado(),
                socio.getFechaCreacion(),
                socio.getFechaModificacion()
        );
    }

    private String normalizar(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
