package com.despacho.backend.controller;

import com.despacho.backend.model.Articulo;
import com.despacho.backend.model.Usuario;
import com.despacho.backend.repository.UsuarioRepository;
import com.despacho.backend.services.ArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService articuloService;
    private final UsuarioRepository usuarioRepository;
    
    // ── Rutas PÚBLICAS ─────────────────────────────────

    // GET http://localhost:8084/api/public/articulos
    @GetMapping("/api/public/articulos")
    public List<Articulo> obtenerArticulosPublicos() {
        return articuloService.obtenerPublicados();
    }

    // GET http://localhost:8084/api/public/articulos/1
    // Detalle de un artículo específico para /blog/:id
    @GetMapping("/api/public/articulos/{id}")
    public ResponseEntity<Articulo> obtenerArticuloPublico(@PathVariable Long id) {
        Articulo articulo = articuloService.obtenerPorId(id);
        // Seguridad extra: si alguien intenta ver un borrador
        // por su id directamente, lo bloqueamos igual
        if (!Boolean.TRUE.equals(articulo.getPublicado())) {
            throw new RuntimeException("Artículo no disponible");
        }
        return ResponseEntity.ok(articulo);
    }

    // ── Rutas del ADMIN ────────────────────────────────

    // GET http://localhost:8084/api/admin/articulos
    @GetMapping("/api/admin/articulos")
    public List<Articulo> obtenerTodos() {
        return articuloService.obtenerTodos();
    }

    // GET http://localhost:8084/api/admin/articulos/1
    @GetMapping("/api/admin/articulos/{id}")
    public ResponseEntity<Articulo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.obtenerPorId(id));
    }

    // POST http://localhost:8084/api/admin/articulos
    @PostMapping("/api/admin/articulos")
    public ResponseEntity<Articulo> crear(@RequestBody Articulo articulo) {
        // Tomamos el email del usuario autenticado
        // que JwtFilter dejó guardado en el contexto de seguridad
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Usuario autor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        articulo.setAutor(autor);
        return ResponseEntity.ok(articuloService.guardar(articulo));
    }

    // PUT http://localhost:8084/api/admin/articulos/1
    @PutMapping("/api/admin/articulos/{id}")
    public ResponseEntity<Articulo> actualizar(
            @PathVariable Long id,
            @RequestBody Articulo articulo) {
        return ResponseEntity.ok(articuloService.actualizar(id, articulo));
    }

    // PUT http://localhost:8084/api/admin/articulos/1/toggle
    @PutMapping("/api/admin/articulos/{id}/toggle")
    public ResponseEntity<Articulo> togglePublicado(@PathVariable Long id) {
        return ResponseEntity.ok(articuloService.togglePublicado(id));
    }

    // DELETE http://localhost:8084/api/admin/articulos/1
    @DeleteMapping("/api/admin/articulos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        articuloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
