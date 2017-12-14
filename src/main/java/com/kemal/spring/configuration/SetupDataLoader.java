package com.kemal.spring.configuration;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private UserService userService;

    private RoleService roleService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SetupDataLoader(UserService userService, RoleService roleService, BCryptPasswordEncoder
            bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        final Role adminRole = roleService.findByName("ROLE_ADMIN");
        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(adminRole);

        final Role userRole = roleService.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        createUserIfNotFound("admin@gmail.com", "Admin", "Admin",
                "admin", "admin", adminRoles);
        createUserIfNotFound("user1@gmail.com", "User1", "User1",
                "user1", "user1", userRoles);
        createUserIfNotFound("user2@gmail.com", "User2", "User2",
                "user2", "user2", userRoles);
        createUserIfNotFound("user3@gmail.com", "User3", "User3",
                "user3", "user3", userRoles);
        createUserIfNotFound("user4@gmail.com", "User4", "User4",
                "user4", "user4", userRoles);

        alreadySetup = true;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name) {
        Role role = roleService.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleService.saveRole(role);
        }
        return role;
    }

    @Transactional
    private final void createUserIfNotFound(final String email, String name, String surname, String username, String password, List<Role> userRoles) {
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setEmail(email);
            user.setRoles(userRoles);
            user.setEnabled(true);
            userService.saveUser(user);
        }
    }
}