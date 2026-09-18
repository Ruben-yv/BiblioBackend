package com.example.BiblioBackend.service.service;

import com.example.BiblioBackend.dto.LibroRequestDTO;
import com.example.BiblioBackend.dto.LibroResponseDTO;
import com.example.BiblioBackend.service.generic.CrudService;
import org.springframework.transaction.annotation.Transactional;

public interface LibroService extends CrudService<LibroRequestDTO, LibroResponseDTO, Long> {
    @Transactional
    LibroResponseDTO update(Long aLong, LibroRequestDTO t);
}
