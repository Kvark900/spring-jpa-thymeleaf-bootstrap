package com.kemal.spring.configuration;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.RoleRepository;
import com.kemal.spring.domain.User;
import com.kemal.spring.domain.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setName("Admin");
        user.setSurname("Admin");
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

}