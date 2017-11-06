package com.kemal.spring.service;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.RoleRepository;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
