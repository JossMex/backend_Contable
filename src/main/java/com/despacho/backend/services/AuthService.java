package com.despacho.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.despacho.backend.dto.LoginRequest;
import com.despacho.backend.dto.LoginResponse;
import com.despacho.backend.dto.RegisterRequest;
import com.despacho.backend.model.Usuario;
import com.despacho.backend.repository.UsuarioRepository;
import com.despacho.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(()
                        -> new RuntimeException("Credenciales incorrectas"));


        // Verificar que la contraseña coincide con la encriptada
        boolean coincide = passwordEncoder.matches(
                request.getPassword(), usuario.getPassword());
        System.out.println("🔑 Password coincide: " + coincide);

        if (!coincide) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // Generar  token con email y rol
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name()
        );


        return new LoginResponse(
                token,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }

    public String register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya esta registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        // Encriptamos el password antes de guardarlo
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Usuario.Rol.valueOf(request.getRol()));

        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
    }

}
