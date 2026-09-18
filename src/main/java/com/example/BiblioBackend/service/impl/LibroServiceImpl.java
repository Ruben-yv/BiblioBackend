package com.example.BiblioBackend.service.impl;

import com.example.BiblioBackend.dto.LibroRequestDTO;
import com.example.BiblioBackend.dto.LibroResponseDTO;
import com.example.BiblioBackend.entity.Genero;
import com.example.BiblioBackend.entity.Libro;
import com.example.BiblioBackend.exception.RecursoNoEncontradoException;
import com.example.BiblioBackend.exception.ReglaNegocioException;
import com.example.BiblioBackend.repository.GeneroRepository;
import com.example.BiblioBackend.repository.LibroRepository;
import com.example.BiblioBackend.service.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServiceImpl implements LibroService {
    private static final Logger LOG = LoggerFactory.getLogger(LibroServiceImpl.class);

    private final LibroRepository libroRepository;
    private final GeneroRepository generoRepository;

    public LibroServiceImpl(LibroRepository libroRepository, GeneroRepository generoRepository) {
        this.libroRepository = libroRepository;
        this.generoRepository = generoRepository;
    }

    @Override
    @Transactional
    public LibroResponseDTO create(LibroRequestDTO t) {
        String titulo = t.getTitulo().trim();
        if (libroRepository.existsByTituloIgnoreCase(titulo)){
            throw new ReglaNegocioException("Ya existe un producto con el titulo " + titulo);
        }
        Genero genero = generoRepository.findById(t.getGeneroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Genero no encontrado con id: " + t.getGeneroId()
                ));
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(t.getAutor());
        libro.setEstado(t.getEstado());
        libro.setStock(t.getStock());
        libro.setIsbn(t.getIsbn());
        libro.setCostoReposicion(t.getCostoReposicion());
        libro.setGenero(genero);

        Libro libCreado = libroRepository.save(libro);
        return convertirResponse(libCreado);
    }

    @Transactional
    @Override
    public LibroResponseDTO update(Long aLong, LibroRequestDTO t) {
        Libro libro = libroRepository.findById(aLong).orElseThrow(() ->
                new RecursoNoEncontradoException(
                        "Libro no encontrado con id: " + aLong
                )
        );
        Genero genero = generoRepository.findById(t.getGeneroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Genero no encontrado con id: " + t.getGeneroId()
                ));
        libro.setTitulo(t.getTitulo());
        libro.setEstado(t.getEstado());
        libro.setStock(t.getStock());
        libro.setIsbn(t.getIsbn());
        libro.setCostoReposicion(t.getCostoReposicion());
        libro.setGenero(genero);
        Libro libActualizado = libroRepository.save(libro);
        return convertirResponse(libActualizado);

    }
    @Override
    @Transactional
    public LibroResponseDTO read (Long aLong){
        Libro libro = libroRepository.findById(aLong)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con id: " + aLong
                        )
                );
        return convertirResponse(libro);
    }

    @Override
    @Transactional
    public void delete (Long aLong){
        Libro libro = libroRepository.findById(aLong).orElseThrow(()->
                new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + aLong
                )
        );
        libroRepository.delete(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<LibroResponseDTO> readAll () {
        return libroRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }
    private LibroResponseDTO convertirResponse(Libro libro){
        return new LibroResponseDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getCostoReposicion(),
                libro.getStock(),
                libro.getEstado(),
                libro.getFechaCreacion(),
                libro.getFechaModificacion(),
                libro.getGenero().getId(),
                libro.getGenero().getNombre()
        );
    }
}
