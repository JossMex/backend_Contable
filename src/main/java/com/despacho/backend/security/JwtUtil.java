package com.despacho.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    
    // Convierte el secret en una clave criptográfica
    // que JWT puede usar para firmar
    private Key getSigningKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera el token con el email y el rol del usuario
    // El token "caduca" después de jwt.expiration milisegundos
    public String generateToken(String email, String rol){
        return Jwts.builder()
        .subject(email)
        .claim("rol", rol)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey())
        .compact();
    }

    // Lee elemail que está guardaro dentro del token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Lee el rol que stá guardado dentro del token
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // Verifica que el token sea válido (no vencido,, no alterado)
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e ) {
            return false;
        }
    }

    // Decidifica el token y extrae toda su información interna
    private Claims getClaims(String token) {
        return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(
            Base64.getDecoder().decode(secret)))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
