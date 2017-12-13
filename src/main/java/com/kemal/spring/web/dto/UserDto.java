package com.kemal.spring.web.dto;

import com.kemal.spring.customAnnotations.PasswordMatches;
import com.kemal.spring.customAnnotations.ValidEmail;
import com.kemal.spring.domain.Role;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keno&Kemo on 22.10.2017..
 */
@PasswordMatches
@Data
public class UserDto {

    private Long id;

    @NotBlank (message = "Name is required")
    private String name;

    @NotBlank (message = "Surname is required")
    private String surname;

    @NotBlank (message = "Username is required")
    private String username;

    @ValidEmail
    @NotBlank (message = "Email is required")
    private String email;

    @NotBlank (message = "Password is required")
    private String password;

    @NotBlank (message = "Matching password is required")
    private String matchingPassword;

    private List<Role> roles = new ArrayList<>();
}
