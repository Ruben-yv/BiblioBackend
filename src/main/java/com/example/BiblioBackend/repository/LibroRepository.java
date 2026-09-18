package com.example.BiblioBackend.repository;

import com.example.BiblioBackend.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTituloIgnoreCase(String titulo);
    boolean existsByTituloIgnoreCaseAndIdNot(String titulo, long id);

    List<Libro> findByGeneroId(Long generoId);
    boolean existsByGeneroId(Long generoId);

}
