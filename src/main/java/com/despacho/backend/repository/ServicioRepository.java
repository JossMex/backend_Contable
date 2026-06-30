package com.despacho.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.despacho.backend.model.Servicio;


public interface  ServicioRepository extends JpaRepository<Servicio, Long>{

    // Para la página pública: solo los activos ordenados
    // Spring genera el SQL: WHERE activo = true ORDER BY orden ASC
    List<Servicio> findByActivoTrueOrderByOrdenAsc();

    // Para el CMD: todos ordenados sin filtrar por activo
    List<Servicio> findAllByOrderByOrdenAsc();
    
}
