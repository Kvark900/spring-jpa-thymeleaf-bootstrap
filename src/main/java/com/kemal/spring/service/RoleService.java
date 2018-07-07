package com.kemal.spring.service;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.RoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Cacheable("cache.allRoles")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "cache.roleByName", key = "#name", unless = "#result == null")
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Cacheable(value = "cache.roleById", key = "#id", unless = "#result == null")
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
    //==================================================================================
    //endregion

    @CacheEvict(value = {"cache.allRoles" , "cache.roleByName", "cache.roleById"}, allEntries = true)
    public void save(Role role) {
        roleRepository.save(role);
    }

    public boolean checkIfRoleNameIsTaken(List<Role> allRoles, Role role, Role persistedRole) {
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
