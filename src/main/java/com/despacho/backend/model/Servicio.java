package com.despacho.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table( name="servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=150)
    private String titulo;

    @Column(columnDefinition="TEXT")
    private String descripcion;

    @Column(length=50)
    private String icono; // nombre del icono a mostrar en el frontend

    @Column(nullable=false)
    private Integer  orden = 0; // controla en que posición aparece

    @Column(nullable=false)
    private Boolean activo = true; // permite "ocultar" sin borrar

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
    
}
