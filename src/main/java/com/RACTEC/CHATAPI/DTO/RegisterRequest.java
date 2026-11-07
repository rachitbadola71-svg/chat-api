package com.RACTEC.CHATAPI.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegisterRequest(
      @NotBlank(message = "username is required") String username,
      @NotBlank(message = "email is required") @Email(message = "email is not valid") String email,
      @NotBlank(message = "password is required") @Size(min=6 ,message = "password should be atleast 6 character")   String password) {
}
