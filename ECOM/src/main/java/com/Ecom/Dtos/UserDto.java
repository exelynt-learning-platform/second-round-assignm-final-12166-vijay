package com.Ecom.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer userId;
    @NotBlank
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "user email is required")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{5,}$",message = "Password must contain uppercase, lowercase, a number, and be at least 5 characters long.")
    private String password;

    private String userImageUrl;

}