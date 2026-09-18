package com.example.BiblioBackend.service.impl;

import com.example.BiblioBackend.dto.GeneroRequestDTO;
import com.example.BiblioBackend.dto.GeneroResponseDTO;
import com.example.BiblioBackend.entity.Genero;
import com.example.BiblioBackend.exception.RecursoNoEncontradoException;
import com.example.BiblioBackend.exception.ReglaNegocioException;
import com.example.BiblioBackend.repository.GeneroRepository;
import com.example.BiblioBackend.service.service.GeneroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class GeneroServiceImpl implements GeneroService {
    private static final Logger LOG = LoggerFactory.getLogger(GeneroServiceImpl.class);

    private final GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    @Transactional
    public GeneroResponseDTO create(GeneroRequestDTO t) {
        String nombre = t.getNombre().trim();
        if (generoRepository.existsByNombreIgnoreCase(nombre)){
            throw new ReglaNegocioException("Ya existe un genero con el nombre " + nombre);
        }
        Genero genero = new Genero();
        genero.setNombre(nombre);
        genero.setDescripcion(t.getDescripcion());
        genero.setEstado(t.getEstado());

        Genero genCreado = generoRepository.save(genero);
        return convertirResponse(genCreado);
    }

    @Transactional
    @Override
    public GeneroResponseDTO update(Long aLong, GeneroRequestDTO t) {
        Genero genero = generoRepository.findById(aLong).orElseThrow(()->
                new RecursoNoEncontradoException(
                        "Genero no encontrado con id: " + aLong
                )
        );
        genero.setNombre(t.getNombre());
        genero.setDescripcion(t.getDescripcion());
        genero.setEstado(t.getEstado());
        Genero genActualizado = generoRepository.save(genero);
        return convertirResponse(genActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public GeneroResponseDTO read(Long aLong) {
        Genero genero = generoRepository.findById(aLong)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Categoria no encontrada con id: " + aLong
                        )
                );
        return convertirResponse(genero);
    }

    @Override
    @Transactional
    public void delete(Long aLong) {
        Genero genero = generoRepository.findById(aLong).orElseThrow(()->
                new RecursoNoEncontradoException(
                        "Genero no encontrado con id: " + aLong
                )
        );
        generoRepository.delete(genero);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<GeneroResponseDTO> readAll() {
        return generoRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    private GeneroResponseDTO convertirResponse(Genero genero){
        return new GeneroResponseDTO(
                genero.getId(),
                genero.getNombre(),
                genero.getDescripcion(),
                genero.getEstado(),
                genero.getFechaCreacion(),
                genero.getFechaModificacion()
        );
    }
}
