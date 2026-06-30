package com.despacho.backend.services;

import org.springframework.stereotype.Service;

import com.despacho.backend.model.Servicio;
import com.despacho.backend.repository.ServicioRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    // Para el panel admin -> todos los servicios
    public List<Servicio> obtenerTodos(){
        return servicioRepository.findAllByOrderByOrdenAsc();
    }

    // Para la página pública -> solo activos ordenados
    // Esta es la que llama Angular cuando carga /servicios
    public List<Servicio> obtenerActivos(){
        return servicioRepository.findByActivoTrueOrderByOrdenAsc();
    }

    public Servicio obtenerPorId(Long id){
        return servicioRepository.findById(id)
        .orElseThrow(() -> 
    new RuntimeException("Servicio no encontrado con id: " + id));
    }

    public Servicio guardar(Servicio servicio){
        //Validaciones basicas
        if(servicio.getTitulo() == null || servicio.getTitulo().trim().isEmpty()){
            throw new RuntimeException("El título del servicio es obligatorio");
        }

        // Si no se especifica orden, lo ponemos al final
        if(servicio.getOrden() == null) {
            int total = servicioRepository.findAll().size();
            servicio.setOrden(total + 1);
        }
        return servicioRepository.save(servicio);
    }

    public Servicio actualizar(Long id, Servicio servicioNuevo){
        // Primero verificamos que existe
        Servicio servicioExistente = obtenerPorId(id);

        // Actualizamos campo por campo
        // Así conservamos la fecha de creación y otros datos internos
        servicioExistente.setTitulo(servicioNuevo.getTitulo());
        servicioExistente.setDescripcion(servicioNuevo.getDescripcion());
        servicioExistente.setIcono(servicioNuevo.getIcono());
        servicioExistente.setOrden(servicioNuevo.getOrden());
        servicioExistente.setActivo(servicioNuevo.getActivo());

        return servicioRepository.save(servicioNuevo);
    }

    // Activa o desactiva un servicio sin eliminarlo
    // El admin puede "apagar" un servicio temporalmente
    public Servicio toggleActivo(Long id){
        Servicio servicio = obtenerPorId(id);
        servicio.setActivo(!servicio.getActivo());
        return servicioRepository.save(servicio);
    }

    public void eliminar(Long id){
        obtenerPorId(id);
        servicioRepository.deleteById(id);
    }

    
}
