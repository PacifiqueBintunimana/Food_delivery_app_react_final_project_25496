package com.paccy.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;


    private String role;
    private String status;
}
