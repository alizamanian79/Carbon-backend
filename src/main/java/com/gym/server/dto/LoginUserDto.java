package com.gym.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {

    @Size(min = 11, max = 11 ,message ="شماره تلفن باید 11 رقم باشد")
    @NotBlank(message = "شماره تلفن اجباری است")
    private String phoneNumber;

    @NotBlank(message = "رمز عبور اجباری است")
    @NotNull(message = "رمز عبور نمیتواند خالی باشد")
    private String password;

}