package com.example.BiblioBackend.repository;

import com.example.BiblioBackend.entity.DetallePrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {
}
