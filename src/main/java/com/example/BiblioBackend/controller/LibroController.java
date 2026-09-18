package com.example.BiblioBackend.controller;

import com.example.BiblioBackend.dto.LibroRequestDTO;
import com.example.BiblioBackend.dto.LibroResponseDTO;
import com.example.BiblioBackend.service.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }
    @GetMapping
    public ResponseEntity<Iterable<LibroResponseDTO>> findAll(){
        return ResponseEntity.ok(
                libroService.readAll()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(libroService.read(id)
        );
    }
    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(@Valid @RequestBody LibroRequestDTO requestDTO){
        LibroResponseDTO response = libroService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequestDTO requestDTO){
        LibroResponseDTO response = libroService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<LibroRequestDTO> delete(
            @PathVariable Long id){
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}