package com.kemal.spring.service;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.domain.UserRepository;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Keno&Kemo on 18.10.2017..
 */

@Service
public class UserService{

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private RoleService roleService;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, RoleService
            roleService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    //region find methods
    //==============================================================================================
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(Long id) {
        return userRepository.findById(id);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //==============================================================================================
    //endregion

    public void save (User user) {
        userRepository.save(user);
    }
    public void delete(Long id){
        userRepository.delete(id);
    }

    public  User createNewAccount(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList(roleService.findByName("ROLE_USER")));
        return user;

    }

    public List<Role> getAssignedRolesList(UserUpdateDto userUpdateDto) {
        Map<Long, Role> assignedRoleMap = new HashMap<>();
        List<Role> roles = userUpdateDto.getRoles();
        for (Role role : roles) {
            assignedRoleMap.put(role.getId(), role);
        }

        List<Role> userRoles = new ArrayList<>();
        List<Role> allRoles = roleService.findAll();
        for (Role role : allRoles) {
            if (assignedRoleMap.containsKey(role.getId())) {
                userRoles.add(role);
            } else {
                userRoles.add(null);
            }
        }
        return userRoles;
    }

    public boolean checkIfEmailIsTaken(List<User> allUsers, UserUpdateDto userUpdateDto,
                                              User persistedUser){
        boolean emailAlreadyExists = false;
        for (User user : allUsers) {
            //Check if the email is edited and if it is taken
            if (!userUpdateDto.getEmail().equals(persistedUser.getEmail())
                    && userUpdateDto.getEmail().equals(user.getEmail())) {
                emailAlreadyExists = true;
            }
        }
        return emailAlreadyExists;
    }

    public boolean checkIfUsernameIsTaken(List<User> allUsers, UserUpdateDto userUpdateDto,
                                                 User persistedUser){
        boolean usernameAlreadyExists = false;
        for (User user : allUsers) {
            //Check if the email is edited and if it is taken
            if (!userUpdateDto.getEmail().equals(persistedUser.getUsername())
                    && userUpdateDto.getEmail().equals(user.getUsername())) {
                usernameAlreadyExists = true;
            }
        }
        return usernameAlreadyExists;
    }
}
