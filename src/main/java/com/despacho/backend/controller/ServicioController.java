package com.despacho.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.despacho.backend.model.Servicio;
import com.despacho.backend.services.ServicioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    //--- Rutas PÚBLICAS (/api/public/)---
    // Sin JWT -> cualquier visitante puede acceder
    // Angular las llama al cargar la página de los servicios

    // GET http://localhost:8084/api/public/servicios
    @GetMapping("/api/public/servicios")
    public List<Servicio> obtenerServiciosPublicos(){
        // Solo devuelve los activos ordenados
        return servicioService.obtenerActivos();
    }

    //---Rutas del ADMIN (/api/admin/servicios)
    // Con JWT -> solo usuarios autenticados
    // Las usa el panel CMS del admin

    // GET http://localhost:8084/api/admin/servicios
    @GetMapping("/api/admin/servicios")
    public List<Servicio> obtenerTodos(){
        // Devuelve todos incluyendo los desactivados
        return servicioService.obtenerTodos();
    }

    // GET http://localhost:8084/api/admin/servicios/1
    @GetMapping("/api/admin/servicios/{id}")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable  Long id){
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    // POST http://localhost:8084/api/admin/servicios
    @PostMapping("/api/admin/servicios")
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.guardar(servicio));
    }

    //PUT http://localhost:8084/api/admin/servicios/1
    @PutMapping("/api/admin/servicios/{id}")
    public ResponseEntity<Servicio> actuallizar(
        @PathVariable Long id,
        @RequestBody Servicio servicio ) {
            return ResponseEntity.ok(servicioService.actualizar(id, servicio));
        }

    //PUT http://localhost:8084/api/admin/servicios/1/toggle
    // Activa o desactiva sin eliminar
    @PutMapping("/api/admin/servicios/{id}/toggle")
    public ResponseEntity<Servicio> toggleActivo(@PathVariable Long id){
        return ResponseEntity.ok(servicioService.toggleActivo(id));
    }

    // DELETE http://localhost:8084/api/admin/servicios/1
    @DeleteMapping("/api/admin/servicios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
