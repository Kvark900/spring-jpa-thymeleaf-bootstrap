package com.kemal.spring.web.controllers.adminControllers;

import com.kemal.spring.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
