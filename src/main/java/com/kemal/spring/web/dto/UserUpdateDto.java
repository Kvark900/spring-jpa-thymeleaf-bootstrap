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

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @ValidEmail
    @NotBlank
    private String email;

    private boolean enabled;

    private List<Role> roles = new ArrayList<>();
}