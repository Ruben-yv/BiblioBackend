package com.example.BiblioBackend.service.service;

import com.example.BiblioBackend.dto.GeneroRequestDTO;
import com.example.BiblioBackend.dto.GeneroResponseDTO;
import com.example.BiblioBackend.service.generic.CrudService;
import org.springframework.transaction.annotation.Transactional;

public interface GeneroService extends CrudService<GeneroRequestDTO, GeneroResponseDTO, Long> {
    @Transactional
    GeneroResponseDTO update(Long aLong, GeneroRequestDTO t);
}
