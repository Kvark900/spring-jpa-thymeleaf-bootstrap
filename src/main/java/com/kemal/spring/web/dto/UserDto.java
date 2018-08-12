package com.kemal.spring.web.dto;

import com.kemal.spring.customAnnotations.PasswordMatches;
import com.kemal.spring.customAnnotations.ValidEmail;
import com.kemal.spring.domain.Role;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Created by Keno&Kemo on 22.10.2017..
 */
@PasswordMatches
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

    private boolean enabled;

    public UserDto(Long id, String name, String surname, String username, String email, String password, String
            matchingPassword, Set<Role> roles, boolean enabled) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.enabled = enabled;
    }

    public UserDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
