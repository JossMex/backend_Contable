package com.despacho.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.despacho.backend.model.MensajeContacto;
import java.util.List;


public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long>{

    // Para el panel de admin: todos los mensajes, los mas nuevos primero
    List<MensajeContacto> findAllByOrderByCreatedAtDesc();

    // Para mostrar el contador de mensajes no leídos en el dashboard
    // Spring genera: SELECT COUNT(*) WHERE leido = false
    long countByLeidoFalse();
}
