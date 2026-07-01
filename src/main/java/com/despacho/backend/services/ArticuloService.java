package com.despacho.backend.services;

import org.springframework.stereotype.Service;

import com.despacho.backend.model.Articulo;
import com.despacho.backend.repository.ArticuloRepository;
import java.util.List;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticuloService {
 
    private final ArticuloRepository articuloRepository;

    // Para el admin -> todos los artículos (borradores y publicados)
    public List<Articulo> obtenerTodos(){
        return articuloRepository.findAllByOrderByCreatedAtDesc();
    }

    // Para la web pública solo los publicados
    public List<Articulo> obtenerPublicados(){
        return articuloRepository.findByPublicadoTrueOrderByFechaPublicacionDesc();
    }

    public Articulo obtenerPorId(Long id){
        return articuloRepository.findById(id)
        .orElseThrow(()->
    new RuntimeException("Artículo no encontrado con id: " + id));
    }

    public Articulo guardar(Articulo articulo){
        if(articulo.getTitulo() == null || articulo.getTitulo().trim().isEmpty()){
            throw new RuntimeException("El título del artículo es obligatorio");
        }

        // Si se crea ya como publicado, asignamos
        // la fecha de publicación en este momento
        if(Boolean.TRUE.equals(articulo.getPublicado())
        && articulo.getFechaPublicacion() == null){
            articulo.setFechaPublicacion(LocalDateTime.now());
        }
        return articuloRepository.save(articulo);
    }

    public Articulo actualizar(Long id, Articulo articuloNuevo){
        Articulo articuloExistente = obtenerPorId(id);

        boolean eraPublicado = Boolean.TRUE.equals(articuloExistente.getPublicado());
        boolean seraPublicado = Boolean.TRUE.equals(articuloNuevo.getPublicado());

        articuloExistente.setTitulo(articuloNuevo.getTitulo());
        articuloExistente.setResumen(articuloNuevo.getResumen());
        articuloExistente.setContenido(articuloNuevo.getContenido());
        articuloExistente.setImagenUrl(articuloNuevo.getImagenUrl());
        articuloExistente.setPublicado(articuloNuevo.getPublicado());

        // Caso clave: pasó de BORRADOR a PUBLICADO por primera vez
        // -> le asigamos la fecha de publicación AHORA
        if(!eraPublicado && seraPublicado) {
            articuloExistente.setFechaPublicacion(LocalDateTime.now());
        }
        // Si ya estaba publicado, la FECHA no se vuelve a tocar,
        // así conservamos la fecha original de la publicación
        return articuloRepository.save(articuloExistente);
    }

    // Atajo rápido para publicar/ocultar sin mandar todo el objeto
    public Articulo togglePublicado(Long id){
        Articulo articulo = obtenerPorId(id);
        boolean nuevoEstado = !Boolean.TRUE.equals(articulo.getPublicado());
        articulo.setPublicado(nuevoEstado);

        if(nuevoEstado && articulo.getFechaPublicacion() == null){
            articulo.setFechaPublicacion(LocalDateTime.now());
        }
        return articuloRepository.save(articulo);
    }

    public void eliminar(Long id){
        obtenerPorId(id);
        articuloRepository.deleteById(id);
    }
}
