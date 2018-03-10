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

    //region Find methods
    //==================================================================================
    public List<Role> findAll(){return roleRepository.findAll();}
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
    public Role findById(Long id) {return roleRepository.findById(id);}
    //==================================================================================
    //endregion

    //Save
    public void save(Role role){
        roleRepository.save(role);
    }

    public boolean checkIfRoleNameIsTaken (List<Role> allRoles, Role role, Role persistedRole){
        boolean roleNameAlreadyExists = false;
        for (Role role1 : allRoles) {
            //Check if the role name is edited and if it is taken
            if (!role.getName().equals(persistedRole.getName())
                    && role.getName().equals(role1.getName())) {
                roleNameAlreadyExists = true;
            }
        }
        return roleNameAlreadyExists;
    }


}
