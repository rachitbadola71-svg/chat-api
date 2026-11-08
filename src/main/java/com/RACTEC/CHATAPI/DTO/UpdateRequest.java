package com.RACTEC.CHATAPI.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest {

    @Size(min = 3 , max = 50)
    private String username;

    @Email
    @Size(min = 5 , max = 100)
    private String email;

    @Size(min = 6 , max = 100)
    private String password;


}
