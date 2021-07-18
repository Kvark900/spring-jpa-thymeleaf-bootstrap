package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.web.dto.RoleDto;
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
    public static final String REDIRECT_ADMIN_PAGE_ROLES = "redirect:/adminPage/roles";
    public static final String ADMIN_PAGE_ROLE_EDIT_ROLE = "adminPage/role/editRole";
    private final RoleService roleService;

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
        ModelAndView modelAndView = new ModelAndView(ADMIN_PAGE_ROLE_EDIT_ROLE);
        modelAndView.addObject("role", role.get());
        return modelAndView;
    }

    @PostMapping("/roles/{id}")
    public String updateRole(Model model, @PathVariable Long id,
                             @ModelAttribute("oldRole") @Valid final Role role,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        Optional<Role> persistedRole = roleService.findById(id);
        List<Role> allRoles = roleService.findAll();

        boolean roleNameAlreadyExists = roleService.checkIfRoleNameIsTaken(allRoles, role, persistedRole.get());
        boolean hasErrors = roleNameAlreadyExists || bindingResult.hasErrors();

        if (roleNameAlreadyExists) bindingResult.rejectValue("name", "roleNameAlreadyExists");

        if (hasErrors) {
            model.addAttribute("role", role);
            model.addAttribute("org.springframework.validation.BindingResult.role", bindingResult);
            return ADMIN_PAGE_ROLE_EDIT_ROLE;
        }
        roleService.save(role);
        redirectAttributes.addFlashAttribute("roleHasBeenUpdated", true);
        return REDIRECT_ADMIN_PAGE_ROLES;
    }

    @GetMapping("/roles/newRole")
    public String getAddNewRoleForm(Model model) {
        model.addAttribute("newRole", new RoleDto());
        return "adminPage/role/newRole";
    }

    @PostMapping("/roles/newRole")
    public String saveNewRole(@ModelAttribute("newRole") @Valid final Role newRole,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        boolean roleNameAlreadyExists = roleService.findByName(newRole.getName()) != null;
        boolean hasErrors = roleNameAlreadyExists || bindingResult.hasErrors();
        String formWithErrors = "adminPage/role/newRole";

        if (roleNameAlreadyExists) bindingResult.rejectValue("name", "roleNameAlreadyExists");
        if (hasErrors) return formWithErrors;

        roleService.save(newRole);
        redirectAttributes.addFlashAttribute("roleHasBeenSaved", true);
        return REDIRECT_ADMIN_PAGE_ROLES;
    }
}
