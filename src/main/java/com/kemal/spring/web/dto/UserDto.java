package com.kemal.spring.web.dto;

import com.kemal.spring.customAnnotations.PasswordMatches;
import com.kemal.spring.customAnnotations.ValidEmail;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Keno&Kemo on 22.10.2017..
 */
@PasswordMatches
@Data
public class UserDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @ValidEmail
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String matchingPassword;


}
