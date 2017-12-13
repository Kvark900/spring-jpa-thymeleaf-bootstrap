package com.kemal.spring.web.dto;

import com.kemal.spring.customAnnotations.ValidEmail;
import com.kemal.spring.domain.Role;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keno&Kemo on 03.12.2017..
 */
@Data
public class UserUpdateDto {

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

    private boolean enabled;

    private List<Role> roles = new ArrayList<>();
}