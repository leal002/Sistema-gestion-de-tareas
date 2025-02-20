package com.uniminuto.gestionDeTareas.backend.services;

import org.springframework.stereotype.Service;

import com.uniminuto.gestionDeTareas.backend.config.JWTUtil;
import com.uniminuto.gestionDeTareas.backend.entities.Tarea;
import com.uniminuto.gestionDeTareas.backend.entities.Usuario;
import com.uniminuto.gestionDeTareas.backend.repositories.TareaRepository;
import com.uniminuto.gestionDeTareas.backend.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final JWTUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public TareaService(TareaRepository tareaRepository, JWTUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.tareaRepository = tareaRepository;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    /*crear tarea */
    public Tarea createTask(Tarea task) {
        return tareaRepository.save(task);
    }

    /*Obtener todas las tareas */
    public List<Tarea> getTasksForUser(String token) {
        String username = jwtUtil.extractUsername(token);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if ("Quality manager".equals(usuario.getRole())) {
            // Si es Quality Manager, devuelve todas las tareas
            return tareaRepository.findAll();
        } else {
            // Si no, solo devuelve las tareas del usuario autenticado
            return tareaRepository.findByUsuarioUsername(username);
        }
    }

    /*obtener tarea por ID */

    public Optional<Tarea> getTaskById(Long id) {
        return tareaRepository.findById(id);
    }

    /*Actualizar tarea  */
    public Tarea updateTask(Long id, Tarea tarea) {
        if (tareaRepository.existsById(id)) {
            tarea.setId(id);
            return tareaRepository.save(tarea);
        } else {
            return null;
        }
    }

    /*Borrar tarea */

    public boolean deleteTask(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

