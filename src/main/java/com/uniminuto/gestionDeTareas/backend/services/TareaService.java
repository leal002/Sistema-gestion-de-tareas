package com.uniminuto.gestionDeTareas.backend.services;

import org.springframework.stereotype.Service;

import com.uniminuto.gestionDeTareas.backend.entities.Tarea;
import com.uniminuto.gestionDeTareas.backend.repositories.TareaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public Tarea createTask(Tarea task) {
        return tareaRepository.save(task);
    }

    public List<Tarea> getAllTasks() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> getTaskById(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea updateTask(Long id, Tarea tarea) {
        if (tareaRepository.existsById(id)) {
            tarea.setId(id);
            return tareaRepository.save(tarea);
        } else {
            return null;
        }
    }

    public boolean deleteTask(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

