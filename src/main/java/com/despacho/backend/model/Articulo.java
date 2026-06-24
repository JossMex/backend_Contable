package com.despacho.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String titulo;

    @Column(length=300)
    private String resumen; //texto corto para la tarjeta del blog 

    @Column(columnDefinition = "LONGTEXT")
    private String contenido; //artículo completo

    @Column(name="imagen_url", length=255)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name="autor_id")
    private Usuario autor;

    @Column(nullable=false)
    private Boolean publicado = false; // borrador vs publicado

    @Column(name="fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
    
}
