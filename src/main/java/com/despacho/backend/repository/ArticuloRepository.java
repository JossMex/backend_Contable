package com.despacho.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.despacho.backend.model.Articulo;

public interface  ArticuloRepository extends JpaRepository<Articulo, Long>{

    //Para la página pública /blog:
    //solo artículos publicados, los más nuevos primero
    List<Articulo> findByPublicadoTrueOrderByFechaPublicacionDesc();

    //Para el panel admin: todos sin filtrar, los más nuevos primero
    List<Articulo> findAllByOrderByCreatedAtDesc();
    
}
