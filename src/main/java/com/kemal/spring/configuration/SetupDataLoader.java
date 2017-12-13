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

        /*final User user = new User();
        user.setName("Admin");
        user.setSurname("Admin");
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userService.saveNewUser(user);*/

        createUserIfNotFound("test@test.com", adminRoles);
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
    private final void createUserIfNotFound(final String email, List<Role> userRoles) {
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName("Admin");
            user.setSurname("Admin");
            user.setUsername("admin");
            user.setPassword(bCryptPasswordEncoder.encode("test"));
            user.setEmail("test@test.com");
            user.setRoles(userRoles);
            user.setEnabled(true);
            userService.saveUser(user);
        }
    }

}