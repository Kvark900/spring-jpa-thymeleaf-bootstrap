package com.kemal.spring.web.controllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */
@Controller
@RequestMapping("/adminPage")

public class RolesController {
    private RoleService roleService;

    public RolesController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ModelAndView showRoles(){
        ModelAndView modelAndView = new ModelAndView("adminPage/roles");
        modelAndView.addObject("roles", roleService.getAllRoles());
        return modelAndView;
    }

    @GetMapping("/roles/{id}")
    public ModelAndView editRole(@PathVariable Long id){
        Role oldRole = roleService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/editRole");
        modelAndView.addObject("oldRole", oldRole);
        return modelAndView;
    }

}
