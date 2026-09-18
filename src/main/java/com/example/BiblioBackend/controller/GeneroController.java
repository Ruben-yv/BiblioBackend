package com.example.BiblioBackend.controller;

import com.example.BiblioBackend.dto.GeneroRequestDTO;
import com.example.BiblioBackend.dto.GeneroResponseDTO;
import com.example.BiblioBackend.service.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {
    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }
    @GetMapping
    public ResponseEntity<Iterable<GeneroResponseDTO>> findAll(){
        return ResponseEntity.ok(
                generoService.readAll()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(generoService.read(id)
        );
    }
    @PostMapping
    public ResponseEntity<GeneroResponseDTO> create(@Valid @RequestBody GeneroRequestDTO requestDTO){
        GeneroResponseDTO response = generoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody GeneroRequestDTO requestDTO){
        GeneroResponseDTO response = generoService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneroRequestDTO> delete(
            @PathVariable Long id){
        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}