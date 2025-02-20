package com.uniminuto.gestionDeTareas.backend.controllers;

import com.uniminuto.gestionDeTareas.backend.dto.UserDTO;
import com.uniminuto.gestionDeTareas.backend.entities.Rol;
import com.uniminuto.gestionDeTareas.backend.entities.Tarea;
import com.uniminuto.gestionDeTareas.backend.entities.Usuario;
import com.uniminuto.gestionDeTareas.backend.services.TareaService;
import com.uniminuto.gestionDeTareas.backend.services.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
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

    /*Obtener roles */
    @GetMapping("/roles")
    public List<String> getRoles(){
        return Arrays.stream(Rol.values()) // Convierte los enums a una lista de strings
                     .map(Rol::getDisplayName) // Usa getDisplayName() si usaste @JsonValue en el enum
                    .toList();
    }

    /*Obtener usuarios */
    @GetMapping("/users")
    public List<Usuario> getUsers() {
        return usuarioService.getUsers();
    }

    /*Registrar los usuarios */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String token = usuarioService.registrarUsuario(request.get("username"), request.get("password"), request.get("rol"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    /*Auntenticacion de login */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String token = usuarioService.autenticarUsuario(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    /*Usuario autenticado */
    
    @GetMapping("/autenticado")
    public ResponseEntity<UserDTO> obtenerUsuarioAutenticado(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remueve "Bearer "
        return ResponseEntity.ok(usuarioService.obtenerUsuarioAutenticado(jwt));
    }

    /*Verficar contraseña */
    @PostMapping("/verificarcontraseña")
    public ResponseEntity<Map<String, String>> verificarContraseña(@RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, String> request) {
        String jwt = token.replace("Bearer ", "");
        String password = request.get("password");

        if (usuarioService.verificarContraseña(jwt, password)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Contraseña verificada correctamente");
            return ResponseEntity.ok(response); // 🔹 Responde un JSON válido
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Contraseña incorrecta"));
        }
    }

    /*actualizar perfil */
    @PutMapping("/actualizarperfil")
    public ResponseEntity<UserDTO> actualizarPerfil(@RequestHeader("Authorization") String token,
    @RequestBody UserDTO userDTO) {
    String jwt = token.replace("Bearer ", "");
     String currentPassword = userDTO.getCurrentPassword(); // 🔹 Se obtiene correctamente

        return ResponseEntity.ok(usuarioService.actualizarPerfil(jwt, currentPassword, userDTO));
}

    /*Crear una tarea */
    @PostMapping ("/createTask")
    public ResponseEntity<Tarea> createTask(@RequestBody Tarea tarea) {
        return ResponseEntity.ok(tareaService.createTask(tarea));
    }

    /*obtener todas las tareas */
    @GetMapping("/tasks")
    public ResponseEntity<List<Tarea>> getTasksForUser(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        List<Tarea> tareas = tareaService.getTasksForUser(jwt);
        return ResponseEntity.ok(tareas);
    }

    /*Obtener tarea por ID */
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getTaskById(@PathVariable Long id) {
        Optional<Tarea> task = tareaService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*Actualizar tarea */
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> updateTask(@PathVariable Long id, @RequestBody Tarea tarea) {
        Tarea updatedTarea = tareaService.updateTask(id, tarea);
        return updatedTarea != null ? ResponseEntity.ok(updatedTarea) : ResponseEntity.notFound().build();
    }

    /*Eliminar tarea */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return tareaService.deleteTask(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


}
