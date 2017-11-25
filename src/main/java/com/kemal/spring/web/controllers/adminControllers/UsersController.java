package com.kemal.spring.web.controllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */
@Controller
@RequestMapping("/adminPage")
public class UsersController {
    UserService userService;
    RoleService roleService;

    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ModelAndView showUsers(){
        ModelAndView modelAndView = new ModelAndView("adminPage/users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView editUserForm(@PathVariable Long id){
        User oldUser = userService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/editUser");

        Map<Long, Role> assignedRoleMap = new HashMap<>();
        List<Role> roles = oldUser.getRoles();
        for (Role role : roles) {
            assignedRoleMap.put(role.getId(), role);
        }

        List<Role> userRoles = new ArrayList<>();
        List<Role> allRoles = roleService.getAllRoles();
        for (Role role : allRoles) {
            if(assignedRoleMap.containsKey(role.getId())){
                userRoles.add(role);
            } else {
                userRoles.add(null);
            }
        }

        oldUser.setRoles(userRoles);

        modelAndView.addObject("oldUser", oldUser);
        modelAndView.addObject("rolesList",allRoles);
        return modelAndView;
    }

    @GetMapping("/users/newUser")
    public ModelAndView addNewUserForm (){
        User newUser = new User();
        ModelAndView modelAndView = new ModelAndView("adminPage/newUser");
        modelAndView.addObject("newUser", newUser);
        return modelAndView;

    }


}
