package com.despacho.backend.services;

import org.springframework.stereotype.Service;

import com.despacho.backend.model.MensajeContacto;
import com.despacho.backend.repository.MensajeContactoRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MensajeContactoService {

    private final MensajeContactoRepository mensajeRepository;

    // El visitante envía un mensaje desde el formulario público
    public MensajeContacto guardar(MensajeContacto mensaje){
        if (mensaje.getNombre() == null || mensaje.getNombre().trim().isEmpty()){
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (mensaje.getEmail() == null || mensaje.getEmail().trim().isEmpty()){
            throw new RuntimeException("El email es obligatorio");
        }
        if (mensaje.getMensaje() == null || mensaje.getMensaje().trim().isEmpty()){
            throw new RuntimeException("El mensaje es obligatorio");
        }

        //El campo leido siempre empieza en false
        // el admin lo marcará como leido desde el CMS
        mensaje.setLeido(false);
        return mensajeRepository.save(mensaje);
    }

    // El admin ve todos los mensajes ordenados del más nuevo al más viejo
    public List<MensajeContacto> obtenerTodos(){
        return mensajeRepository.findAllByOrderByCreatedAtDesc();
    }

    public MensajeContacto obtenerPorId(Long id){
        return mensajeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
    }

    //Cupangos mensajes hay sin leer para el contador del dashboard
    public Long contarNoLeidos(){
        return mensajeRepository.countByLeidoFalse();
    }

    // El admin marca un mensaje como leido
    // Funciona como toggle: leído <-> no leído
    public MensajeContacto toggleLeido(Long id) {
        MensajeContacto mensaje = obtenerPorId(id);
        mensaje.setLeido(!mensaje.getLeido());
        return mensajeRepository.save(mensaje);
    }

    // El admin elimina un mensaje definitivamente
    public void eliminar(Long id){
        obtenerPorId(id);
        mensajeRepository.deleteById(id);
    }
    
}
