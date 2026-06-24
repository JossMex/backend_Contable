package com.despacho.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="mensajes_contacto")
public class MensajeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String nombre;

    @Column(nullable=false, length=100)
    private String email;
    
    @Column(length=20)
    private String telefono;

    @Column(length=50)
    private String asunto;

    @Column(nullable=false, columnDefinition="TEXT")
    private String mensaje;

    @Column(nullable=false)
    private Boolean leido=false; //para que el admin sepa cuáles ya revisó
    
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
    
}
