package com.uniminuto.gestionDeTareas.backend.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 🔴 DESHABILITA CSRF PARA PRUEBAS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/app/register", "/app/login", "/app/createTask", "/app/tasks", "/app/{id}", "/users").permitAll() // 🔵 PERMITIR ACCESO A ESTAS RUTAS
                .anyRequest().authenticated() // 🔴 TODAS LAS DEMÁS NECESITAN TOKEN
            )
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

