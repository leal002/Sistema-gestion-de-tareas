package com.uniminuto.gestionDeTareas.backend.services;

import com.uniminuto.gestionDeTareas.backend.config.JWTUtil;
import com.uniminuto.gestionDeTareas.backend.dto.UserDTO;
import com.uniminuto.gestionDeTareas.backend.entities.Usuario;
import com.uniminuto.gestionDeTareas.backend.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /* Registrar un nuevo usuario */
    public String registrarUsuario(String username, String password, String rol) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRole(rol);

        usuarioRepository.save(usuario);

        return jwtUtil.generateToken(username);
    }

    /*Auntenticar los usuarios */

    public String autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent() && passwordEncoder.matches(password, usuarioOpt.get().getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
        
    }

    /* Método para verificar contraseña */
    public boolean verificarContraseña(String token, String currentPassword) {
        String username = jwtUtil.extractUsername(token);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return passwordEncoder.matches(currentPassword, usuario.getPassword());
    }

    /*obtener usuario autenticado */
    public UserDTO obtenerUsuarioAutenticado(String token){
        String username = jwtUtil.extractUsername(token);
        Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        return new UserDTO(usuario.getId(), usuario.getUsername(), null, null, usuario.getRole());
    }

     /* Actualizar perfil con verificación de contraseña */
    public UserDTO actualizarPerfil(String token, String currentPassword, UserDTO userDTO) {
        String username = jwtUtil.extractUsername(token);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar la contraseña actual antes de permitir cambios
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        usuario.setUsername(userDTO.getUsername());
        usuario.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        usuarioRepository.save(usuario);
        return new UserDTO(usuario.getId(), usuario.getUsername(), null, null, usuario.getRole());
    }


    public List<Usuario> getUsers() {
        return usuarioRepository.findAll();
    }
}

