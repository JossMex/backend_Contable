package com.despacho.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.despacho.backend.model.MensajeContacto;
import com.despacho.backend.services.MensajeContactoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MensajeContactoController {

    private final MensajeContactoService mensajeService;

    //-- Ruta PÚBLICA --
    // El visitante envía el formulario de contacto
    // NO necesita token porqyr cualquier visitante puede contactar
    // POST http://localhost:8084/api/public/contacto
    @PostMapping("/api/public/contacto")
    public ResponseEntity<Map<String, String>> enviarMensaje(
            @RequestBody MensajeContacto mensaje) {
        mensajeService.guardar(mensaje);

        //Devolvemos un mensaje de confirmación, no el objeto completo
        //El visitante solo necesita saber que se envío
        return ResponseEntity.ok(
                Map.of("mensaje", "Tu mensaje fue enviado correctamente. " + "Nos pondremos en contacto contigo pronto")
        );
    }

    //----Rutas del ADMIN----
    //GET http://localhost:8084/api/admin/mensajes
    @GetMapping("/api/admin/mensajes")
    public List<MensajeContacto> obtenerTodos() {
        return mensajeService.obtenerTodos();
    }

    //GET http://localhost:8084/api/admin/no-leidos
    // Para el contador del dashboard: "📬 3 mensajes sin leer"
    @GetMapping("/api/admin/mensajes/no-leidos")
    public ResponseEntity<Map<String, Long>> contarNoLeidos() {
        long cantidad = mensajeService.contarNoLeidos();
        return ResponseEntity.ok(Map.of("noLeidos", cantidad));
    }

    //GET http://localhost:8084/api/admin/mensajes/1
    @GetMapping("/api/admin/mensajes/{id}")
    public ResponseEntity<MensajeContacto> obtenerPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(mensajeService.obtenerPorId(id));
    }

    //PUT http://localhost:8084/api/admin/mensajes/1/toggle
    //Marca como leído o no leído
    @PutMapping("/api/admin/mensajes/{id}/toggle")
    public ResponseEntity<MensajeContacto> toggleLeido(
        @PathVariable Long id) {
            return ResponseEntity.ok(mensajeService.toggleLeido(id));
        }
    
    //DELETE http://localhost:8084/api/admin/mensajes/1
    @DeleteMapping("/api/admin/mesnajes/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mensajeService.eliminar(id);
        return ResponseEntity.noContent().build();
        
    }
}
