package com.uniminuto.gestionDeTareas.backend.controllers;

import com.uniminuto.gestionDeTareas.backend.entities.Tarea;
import com.uniminuto.gestionDeTareas.backend.entities.Usuario;
import com.uniminuto.gestionDeTareas.backend.services.TareaService;
import com.uniminuto.gestionDeTareas.backend.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5500") // Permite el acceso desde Live Server en el puerto 5500
@RequestMapping("/app")
public class AppController {

    private final UsuarioService usuarioService;
    private final TareaService tareaService;
    

    public AppController(UsuarioService usuarioService, TareaService tareaService) {
        this.usuarioService = usuarioService;
        this.tareaService = tareaService;
    }

    @GetMapping("/users")
    public List<Usuario> getUsers() {
        return usuarioService.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String token = usuarioService.registrarUsuario(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String token = usuarioService.autenticarUsuario(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping ("/createTask")
    public ResponseEntity<Tarea> createTask(@RequestBody Tarea tarea) {
        return ResponseEntity.ok(tareaService.createTask(tarea));
    }

    @GetMapping ("/tasks")
    public ResponseEntity<List<Tarea>> getAllTasks() {
        List<Tarea> tareas = tareaService.getAllTasks(); 
    return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getTaskById(@PathVariable Long id) {
        Optional<Tarea> task = tareaService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> updateTask(@PathVariable Long id, @RequestBody Tarea tarea) {
        Tarea updatedTarea = tareaService.updateTask(id, tarea);
        return updatedTarea != null ? ResponseEntity.ok(updatedTarea) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return tareaService.deleteTask(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


}
