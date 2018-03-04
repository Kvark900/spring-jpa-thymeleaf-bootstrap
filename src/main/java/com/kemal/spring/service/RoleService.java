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

    //Find methods
    public List<Role> findAll(){return roleRepository.findAll();}
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
    public Role findById(Long id) {return roleRepository.findById(id);}

    //Save
    public void save(Role role){
        roleRepository.save(role);
    }


}
