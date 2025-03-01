package com.gym.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String passwordDecoder;

    
    // getters and setters here...
}