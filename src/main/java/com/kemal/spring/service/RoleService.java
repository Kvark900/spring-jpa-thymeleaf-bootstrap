package com.kemal.spring.service;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Keno&Kemo on 04.11.2017..
 */
@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
    public void saveRole (Role role){
        roleRepository.save(role);
    }
    public List<Role> getAllRoles (){
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id);
    }
}
