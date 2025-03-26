package com.gym.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(min = 11, max = 11 ,message ="شماره تلفن باید 11 رقم باشد")
    @NotBlank(message = "شماره تلفن اجباری است")
    private String phoneNumber;

    private String password;

    private String passwordDecoder;
    private String fullName;
    
    // getters and setters here...
}