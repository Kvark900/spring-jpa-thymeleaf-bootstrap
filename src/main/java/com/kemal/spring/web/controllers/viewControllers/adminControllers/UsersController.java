package com.kemal.spring.web.controllers.viewControllers.adminControllers;

import com.kemal.spring.domain.Role;
import com.kemal.spring.domain.User;
import com.kemal.spring.service.RoleService;
import com.kemal.spring.service.UserDtoService;
import com.kemal.spring.service.UserService;
import com.kemal.spring.service.UserUpdateDtoService;
import com.kemal.spring.web.dto.UserDto;
import com.kemal.spring.web.dto.UserUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Keno&Kemo on 20.11.2017..
 */


@Controller
@RequestMapping("/adminPage")
public class UsersController {
    private UserService userService;
    private RoleService roleService;
    private ModelMapper modelMapper;
    private UserUpdateDtoService userUpdateDtoService;
    private UserDtoService userDtoService;

    public UsersController(UserService userService, RoleService roleService,
                           ModelMapper modelMapper, UserUpdateDtoService userUpdateDtoService,
                           UserDtoService userDtoService) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.userUpdateDtoService = userUpdateDtoService;
        this.userDtoService = userDtoService;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userUpdateDtoService.findAll());
        return "adminPage/users";
    }

    @GetMapping("/users/{id}")
    public String getEditUserForm(@PathVariable Long id, Model model) {
        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);
        List<Role> allRoles = roleService.getAllRoles();
        userUpdateDto.setRoles(userService.getAssignedRolesList(userUpdateDto));

        model.addAttribute("userUpdateDto", userUpdateDto);
        model.addAttribute("rolesList", allRoles);
        return "adminPage/editUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model, @PathVariable Long id,
                             @ModelAttribute("oldUser") @Valid final UserUpdateDto userUpdateDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        User persistedUser = userService.findById(id);
        String formWithErrors = "adminPage/editUser";

        boolean emailAlreadyExists = false;
        boolean usernameAlreadyExists = false;
        boolean hasErrors = false;

        List<User> allUsers = userService.findAll();
        List<Role> allRoles = roleService.getAllRoles();

        for (User user : allUsers) {
            //Check if the email is edited and if it is taken
            if (!userUpdateDto.getEmail().equals(persistedUser.getEmail())
                    && userUpdateDto.getEmail().equals(user.getEmail())) {
                emailAlreadyExists = true;
            }
            //Check if the username is edited and if it is taken
            if (!userUpdateDto.getUsername().equals(persistedUser.getUsername())
                    && userUpdateDto.getUsername().equals(user.getUsername())) {
                usernameAlreadyExists = true;
            }
        }

        if (emailAlreadyExists) {
            bindingResult.rejectValue("email", "emailAlreadyExists",
                    "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists) {
            bindingResult.rejectValue("username", "usernameAlreadyExists",
                    "Oops!  There is already a user registered with the username provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) {
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("userUpdateDto", userUpdateDto);
            model.addAttribute("rolesList", allRoles);
            model.addAttribute("org.springframework.validation.BindingResult.userUpdateDto",
                    bindingResult);
            return formWithErrors;

        } else {
            persistedUser.setName(userUpdateDto.getName());
            persistedUser.setSurname(userUpdateDto.getSurname());
            persistedUser.setUsername(userUpdateDto.getUsername());
            persistedUser.setEmail(userUpdateDto.getEmail());
            persistedUser.setRoles(userService.getAssignedRolesList(userUpdateDto));
            persistedUser.setEnabled(userUpdateDto.isEnabled());
            userService.saveUser(persistedUser);

            redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
            return "redirect:/adminPage/users";
        }
    }

    @GetMapping("/users/newUser")
    public String getAddNewUserForm(Model model) {
        UserDto newUser = new UserDto();
        model.addAttribute("newUser", newUser);
        return "adminPage/newUser";
    }

    @PostMapping("/users/newUser")
    public String saveNewUser(Model model, @ModelAttribute("newUser") @Valid final UserDto newUser,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        UserDto emailAlreadyExists = userDtoService.findByEmail(newUser.getEmail());
        UserDto usernameAlreadyExists = userDtoService.findByUsername(newUser.getUsername());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/newUser";

        if (emailAlreadyExists != null) {
            bindingResult.rejectValue("email", "emailAlreadyExists",
                    "Oops!  There is already a user registered with the email provided.");
            hasErrors = true;
        }

        if (usernameAlreadyExists != null) {
            bindingResult.rejectValue("username", "usernameAlreadyExists",
                    "Oops!  There is already a user registered with the username provided.");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) {
            hasErrors = true;
        }

        if (hasErrors) {
            return formWithErrors;
        } else {
            User user = userService.createNewAccount(newUser);
            user.setEnabled(true);

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("userHasBeenSaved", true);
            return "redirect:/adminPage/users";
        }
    }

}
