package com.uniminuto.gestionDeTareas;




import com.uniminuto.gestionDeTareas.backend.config.JWTUtil;
import com.uniminuto.gestionDeTareas.backend.entities.Usuario;
import com.uniminuto.gestionDeTareas.backend.repositories.UsuarioRepository;
import com.uniminuto.gestionDeTareas.backend.services.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) 
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void testRegistrarUsuario() {
        String username = "testuser";
        String password = "password123";
        String rol = "Quality manager";

        // Mocking
        when(usuarioRepository.findByUsername(username)).thenReturn(java.util.Optional.empty()); // Usuario no existe
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(username)).thenReturn("jwtToken");

        // Ejecutar el método
        String token = usuarioService.registrarUsuario(username, password, rol);

        // Verificar
        assertNotNull(token);
        verify(usuarioRepository, times(1)).save(Mockito.any(Usuario.class));  // Verifica que save() fue llamado
    }

    @Test
    public void testRegistrarUsuarioExistente() {
        String username = "testuser";
        String password = "password123";
        String rol = "Quality Manager";

        // Mocking
        when(usuarioRepository.findByUsername(username)).thenReturn(java.util.Optional.of(new Usuario()));

        // Ejecutar y verificar
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(username, password, rol);
        });

        assertEquals("El usuario ya existe", thrown.getMessage());
    }

    @Test
public void testAutenticarUsuario() {
    String username = "testuser";
    String password = "password123";

    Usuario usuario = new Usuario();
    usuario.setUsername(username);
    usuario.setPassword("encodedPassword");

    // Mocking
    when(usuarioRepository.findByUsername(username)).thenReturn(java.util.Optional.of(usuario));
    when(passwordEncoder.matches(password, usuario.getPassword())).thenReturn(true);
    when(jwtUtil.generateToken(username)).thenReturn("jwtToken");

    // Ejecutar el método
    String token = usuarioService.autenticarUsuario(username, password);

    // Verificar
    assertNotNull(token);
    verify(usuarioRepository, times(1)).findByUsername(username);  // Verifica que findByUsername fue llamado
    verify(jwtUtil, times(1)).generateToken(username);  // Verifica que generateToken fue llamado
}

@Test
public void testAutenticarUsuarioConCredencialesIncorrectas() {
    String username = "testuser";
    String password = "wrongPassword";

    Usuario usuario = new Usuario();
    usuario.setUsername(username);
    usuario.setPassword("encodedPassword");

    // Mocking
    when(usuarioRepository.findByUsername(username)).thenReturn(java.util.Optional.of(usuario));
    when(passwordEncoder.matches(password, usuario.getPassword())).thenReturn(false);

    // Ejecutar y verificar
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
        usuarioService.autenticarUsuario(username, password);
    });

    assertEquals("Credenciales incorrectas", thrown.getMessage());
}

}
