package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.web.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public ModelAndView showRoles() {
        ModelAndView modelAndView = new ModelAndView("adminPage/role/roles");
        modelAndView.addObject("roles", roleService.findAll());
        return modelAndView;
    }

    @GetMapping("/roles/{id}")
    public ModelAndView getEditRoleForm(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/role/editRole");
        modelAndView.addObject("role", role.get());
        return modelAndView;
    }

    @PostMapping("/roles/{id}")
    public String updateRole(Model model, @PathVariable Long id,
                             @ModelAttribute("oldRole") @Valid final Role role,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/role/editRole";
        Optional<Role> persistedRole = roleService.findById(id);
        List<Role> allRoles = roleService.findAll();

        boolean roleNameAlreadyExists = roleService.checkIfRoleNameIsTaken(allRoles, role, persistedRole.get());
        boolean hasErrors = false;

        if (roleNameAlreadyExists) {
            bindingResult.rejectValue("name", "roleNameAlreadyExists", "Oops!  There is already a role registered with the name provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("role", role);
            model.addAttribute("org.springframework.validation.BindingResult.role", bindingResult);
            return formWithErrors;
        } else {
            roleService.save(role);
            redirectAttributes.addFlashAttribute("roleHasBeenUpdated", true);
            return "redirect:/adminPage/roles";
        }
    }

    @GetMapping("/roles/newRole")
    public String getAddNewRoleForm(Model model) {
        model.addAttribute("newUser", new UserDto());
        return "adminPage/role/newRole";
    }

    @PostMapping("/roles/newRole")
    public String saveNewRole(Model model, @ModelAttribute("newRole") @Valid final Role newRole,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Role roleNameAlreadyExists = roleService.findByName(newRole.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/role/newRole";

        if (roleNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "usernameAlreadyExists", "Oops!  There is already a role registered with the name provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        else {
            roleService.save(newRole);
            redirectAttributes.addFlashAttribute("roleHasBeenSaved", true);
            return "redirect:/adminPage/roles";
        }
    }
}
