package com.despacho.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String nombre;
    private String email;
    private String password;
    private String rol; // "ADMIN" o "EDITOR"
    
}
