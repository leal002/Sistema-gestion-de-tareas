package com.uniminuto.gestionDeTareas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String currentPassword;
    private String role;

}
